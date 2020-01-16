package com.fantacg.user.service;


import cn.hutool.core.util.RandomUtil;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RTypeCostant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.constant.RpcConstant;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.user.*;
import com.fantacg.common.utils.*;
import com.fantacg.user.config.CommonRpc;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname MemberService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class MemberService {


    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MemberRoleMapper memberRoleMapper;
    @Autowired
    private AuthLogMapper authLogMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RealNameAuthLogMapper realNameAuthLogMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 新注册
     *
     * @param memberDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result registerMember(MemberDto memberDto) throws Exception {

        //注册类型
        String registerType = memberDto.getRegisterType();
        //判断 如果为空 不能注册
        if (StringUtils.isEmpty(registerType)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }

        // 生成盐
        Member member = new Member();
        member.setId(new IdWorker().nextId());
        String salt = Md5Utils.generate();

        //1 手机 + 验证码 注册
        if (RTypeCostant.REGISTER_TYPE_ONE.equals(registerType)) {
            log.debug("手机号 + 手机 + 验证码 注册");
            if (StringUtils.isEmpty(memberDto.getCode()) && StringUtils.isEmpty(memberDto.getPhone())) {
                return Result.failure(ResultCode.PHONE_CODE_NULL_ERROR);
            }
            boolean b = this.queryMemberByPhone(memberDto.getPhone());
            if (!b) {
                return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
            }

            // 从redis取出验证码
            String key = KeyConstant.MEMBER_REGISTER_CODE_KEY_PREFIX + memberDto.getPhone();
            String codeCache = this.redisTemplate.opsForValue().get(key);
            //判断验证码是否过期
            if (StringUtils.isEmpty(codeCache)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            // 检查验证码是否正确
            if (!memberDto.getCode().equals(codeCache)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }

            //用户名默认为手机号
            member.setUsername(memberDto.getPhone());
            member.setPhone(memberDto.getPhone());
            // 对密码进行加密
            member.setPassword(Md5Utils.encryptPassword("", salt));

        } else if (RTypeCostant.REGISTER_TYPE_TWO.equals(registerType)) {
            log.debug("手机号 + 密码注册");
            // 2 手机 + 密码
            if (StringUtils.isEmpty(memberDto.getPassword()) && StringUtils.isEmpty(memberDto.getPhone())) {
                return Result.failure(ResultCode.PHONE_PWD_NULL_ERROR);
            }
            //判断手机号时候存在
            int i = this.memberMapper.selectLoginMemberByPhone(memberDto.getPhone());
            if (i > 0) {
                return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
            }
            //用户名默认为手机号
            member.setUsername(memberDto.getPhone());
            member.setPhone(memberDto.getPhone());
            // 对密码进行加密
            member.setPassword(Md5Utils.encryptPassword(memberDto.getPassword(), salt));
        } else if (RTypeCostant.REGISTER_TYPE_THREE.equals(registerType)) {
            log.debug("3 邮箱 + 验证码 + 密码");
            //判断参数是否为空
            // 3 邮箱 + 验证码 + 密码
            if (StringUtils.isEmpty(memberDto.getPassword()) && StringUtils.isEmpty(memberDto.getEmail()) && StringUtils.isEmpty(memberDto.getCode())) {
                return Result.failure(ResultCode.PHONE_PWD_NULL_ERROR);
            }

            //判断账号是否存在
            int i = this.memberMapper.selectMemberByEmail(memberDto.getEmail());
            if (i > 0) {
                return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
            }
            // 从redis取出验证码
            String codeCache = this.redisTemplate.opsForValue().get(KeyConstant.MEMBER_CODE_EMAIL_KEY + memberDto.getEmail());
            //判断验证码是否过期
            if (StringUtils.isEmpty(codeCache)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            // 检查验证码是否正确
            if (!memberDto.getCode().equals(codeCache)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            //用户名默认为手机号
            member.setUsername(memberDto.getEmail());
            member.setEmail(memberDto.getEmail());
            // 对密码进行加密
            member.setPassword(Md5Utils.encryptPassword(memberDto.getPassword(), salt));
        }
        member.setSalt(salt);
        member.setCreateTime(new Date());
        member.setIsOpen(1);
        // 写入数据库
        boolean boo = this.memberMapper.insertSelective(member) == 1;
        if (StringUtils.isNotEmpty(memberDto.getAuthPhone())) {
            //查询邀请人是否被认证 如果未认证  无法发送短信帮助他认证
            Member m = new Member();
            m.setPhone(memberDto.getAuthPhone());
            Member mOne = this.memberMapper.selectOne(m);
            if (mOne == null) {
                return Result.failure(ResultCode.ACCOUNT_NOT_AUTH_EXIST);
            }
            RealNameAuthLog log = new RealNameAuthLog();
            log.setMemberId(mOne.getId());
            log.setStatus(1);
            int count = this.realNameAuthLogMapper.selectCount(log);
            if (count == 0) {
                return Result.failure(ResultCode.ACCOUNT_NOT_AUTH_REGISTER);
            }

            String s = RandomUtil.randomNumbers(6);
            // TODO: 2019/3/14 发送短信邀请注册
            ConcurrentHashMap<String, Object> cmap = new ConcurrentHashMap<>();
            cmap.put("name", member.getUsername());
            cmap.put("remark", s);
            boolean sms = CommonRpc.sms(memberDto.getAuthPhone(), RpcConstant.AUTH_URL_CODE, cmap);
            if (!sms) {
                return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
            }

            //发送短信 链接 给邀请人 认证       数据 存入Redis  code 为key 手机号为value 注册人id
            Map<String, Object> map = new HashMap<>();
            map.put("memberId", member.getId());
            //注册人信息
            map.put("username", member.getUsername());
            //邀请人手机号码
            map.put("authPhone", memberDto.getAuthPhone());
            //认证缓存1一天
            this.redisTemplate.opsForValue().set(KeyConstant.UUID_KEY_PHONECODE + s, objectMapper.writeValueAsString(map), 1, TimeUnit.DAYS);
        }

        //赋予默认权限
        MemberRole memberRole = new MemberRole();
        memberRole.setMemberId(member.getId());
        //用户的角色id为1
        memberRole.setRoleId(1L);
        memberRoleMapper.insertSelective(memberRole);

        if (boo) {
            return Result.success(member.getId());
        }

        return Result.failure(ResultCode.REGISTER_EXPIRE);
    }

    /**
     * 管理员添加用户账号
     */
    @Transactional(rollbackFor = Exception.class)
    public Result adminRegisterMember(MemberDto memberDto) {
        log.debug("手机号 + 密码注册");
        Long userId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        //手机 + 密码
        if (StringUtils.isEmpty(memberDto.getPassword()) && StringUtils.isEmpty(memberDto.getPhone())) {
            return Result.failure(ResultCode.PHONE_PWD_NULL_ERROR);
        }
        //判断手机号时候存在
        int i = this.memberMapper.selectLoginMemberByPhone(memberDto.getPhone());
        if (i > 0) {
            return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
        }
        memberDto.setPassword(AesEncrypt.decryptAES(memberDto.getPassword()));
        // 生成盐
        Member member = new Member();
        member.setId(new IdWorker().nextId());
        String salt = Md5Utils.generate();
        //用户名默认为手机号
        member.setUsername(memberDto.getPhone());
        member.setPhone(memberDto.getPhone());
        // 对密码进行加密
        member.setPassword(Md5Utils.encryptPassword(memberDto.getPassword(), salt));
        member.setEmail(memberDto.getEmail());
        member.setRegisterType(1);
        member.setSalt(salt);
        member.setRemark(memberDto.getRemark());
        member.setCreateTime(new Date());
        member.setIsOpen(1);
        // 写入数据库
        this.memberMapper.insertSelective(member);
        //直接认证
        RealNameAuthLog realNameAuthLog = new RealNameAuthLog();
        realNameAuthLog.setMemberId(member.getId());
        realNameAuthLog.setUserId(userId);
        realNameAuthLog.setStatus(1);
        realNameAuthLog.setCreateTime(new Date());
        this.realNameAuthLogMapper.insertSelective(realNameAuthLog);
        //赋予默认功能权限权限
        MemberRole role = new MemberRole();
        role.setMemberId(member.getId());
        //用户的角色id为1
        role.setRoleId(1L);
        boolean boo = this.memberRoleMapper.insertSelective(role) == 1;
        if (boo) {
            return Result.success(ResultCode.DATA_FOUND_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_FOUND_ERROR);
    }

    /**
     * 账号验证码登录
     *
     * @param memberDto
     * @return
     */
    public Result queryMemberByUserName(MemberDto memberDto) {
        if (StringUtils.isEmpty(memberDto.getCode())) {
            return Result.failure(ResultCode.CODE_ERROR);
        }
        try {
            memberDto.setAccount(AesEncrypt.decryptAES(memberDto.getAccount()));
            if (StringUtils.isEmpty(memberDto.getAccount())) {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_PWD_ERROR);
            }
        } catch (Exception e) {
            return Result.failure(ResultCode.PARAMETER_ERROR);
        }
        // 从redis取出验证码 判断验证码是否正确
        String codeCache = this.redisTemplate.opsForValue().get(KeyConstant.MEMBER_LOGIN_CODE_KEY_PREFIX + memberDto.getAccount());
        if (!memberDto.getCode().equals(codeCache)) {
            return Result.failure(ResultCode.CODE_ERROR);
        }
        Member member = new Member();
        member.setUsername(memberDto.getAccount());
        member = this.memberMapper.selectOne(member);
        if (member != null) {
            return Result.success(member);
        }
        return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
    }

    /***
     * 账号（手机号 用户名 邮箱） + 密码 登录
     * @param memberDto
     * @return
     */
    public Result loginMember(MemberDto memberDto) {
        String account = "";
        String password = "";
        long l = System.currentTimeMillis();
        try {
            account = AesEncrypt.decryptAES(memberDto.getAccount());
            password = AesEncrypt.decryptAES(memberDto.getPassword());
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_PWD_ERROR);
            }
        } catch (Exception e) {
            return Result.failure(ResultCode.PARAMETER_ERROR);
        }
        // 查询
        Member member = new Member();
        if (LoginUtils.isEmail(account)) {
            member.setEmail(account);
        } else if (LoginUtils.isPhoneNumberValid(account)) {
            member.setPhone(account);
        } else {
            member.setUsername(account);
        }
        member = this.memberMapper.selectOne(member);
        // 校验用户是否存在
        if (member == null) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
        }
        // 校验密码
        if (!member.getPassword().equals(Md5Utils.encryptPassword(password, member.getSalt()))) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_PWD_ERROR);
        }
        return Result.success(member);
    }


    /**
     * 分页查询所有用户
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public Result queryMemberByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {

        PageHelper.startPage(page, rows);
        Example example = new Example(Member.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("username", "%" + key + "%")
                    .orLike("phone", "%" + key + "%");
        }
        example.setOrderByClause(" create_time DESC ");
        Page<Member> pageInfo = (Page<Member>) memberMapper.selectByExample(example);
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 管理员查询-申请企业认证列表
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public Result getApplyForAuthByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(AuthLog.class);
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<AuthLog> pageInfo = (Page<AuthLog>) authLogMapper.selectByExample(example);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        int count = 0;
        Example example = new Example(MemberRole.class);
        example.createCriteria().andCondition(" member_id =" + id);
        this.memberRoleMapper.deleteByExample(example);
        count = this.memberMapper.deleteByPrimaryKey(id);
        if (count > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS.message());
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

    /**
     * 管理员修改用户信息
     *
     * @param member
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result editUser(Member member, List<Long> roleIds) {
        //不允许修改username
        member.setUsername(null);
        //修改密码先去获取盐
        if (StringUtils.isNotBlank(member.getPassword())) {
            Member m = memberMapper.selectByPrimaryKey(member.getId());
            member.setPassword(Md5Utils.encryptPassword(member.getPassword(), m.getSalt()));
        } else {
            member.setPassword(null);
        }

        //删除用户角色
        Example example = new Example(MemberRole.class);
        example.createCriteria().andCondition(" member_id = " + member.getId());
        memberRoleMapper.deleteByExample(example);
        //新增用户角色
        for (long roleId : roleIds) {
            MemberRole memberRole = new MemberRole();
            memberRole.setRoleId(roleId);
            memberRole.setMemberId(member.getId());
            memberRoleMapper.insertSelective(memberRole);
        }

        int i = this.memberMapper.updateByPrimaryKeySelective(member);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }


    /**
     * 根据uuid查询邀请人信息
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    public Result queryRegisterUrl(String uuid) throws Exception {
        //获取被认证人手机号
        String str = this.redisTemplate.opsForValue().get(KeyConstant.UUID_KEY_PHONECODE + uuid);
        if (StringUtils.isNotEmpty(str)) {
            Map map = objectMapper.readValue(str, Map.class);
            return Result.success(map);
        }
        return Result.failure(ResultCode.NO_ACCESS_ERROR);
    }

    /**
     * 邀请注册认证
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateAuthStatus(String uuid) throws Exception {

        //获取被认证人手机号
        String str = this.redisTemplate.opsForValue().get(KeyConstant.UUID_KEY_PHONECODE + uuid);

        if (StringUtils.isEmpty(str)) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }

        HashMap map = objectMapper.readValue(str, HashMap.class);

        //修改注册的人
        RealNameAuthLog log = new RealNameAuthLog();
        Long memberId = Long.valueOf((Integer) map.get("memberId"));
        log.setMemberId(memberId);
        RealNameAuthLog log1 = this.realNameAuthLogMapper.selectOne(log);

        //判断是否有申请认证信息 Status 0审核中  1同意 2 不同意
        if (log1 == null) {
            String authPhone = (String) map.get("authPhone");
            Member am = new Member();
            am.setPhone(authPhone);
            Member member = this.memberMapper.selectOne(am);
            //添加认证记录
            log.setStatus(1);
            log.setUserId(member.getId());
            log.setType(1);
            log.setCreateTime(new Date());
            this.realNameAuthLogMapper.insertSelective(log);
        } else {
            RealNameAuthLog ulog = new RealNameAuthLog();
            ulog.setStatus(1);
            Example example = new Example(RealNameAuthLog.class);
            example.createCriteria().andCondition(" member_id = " + memberId);
            this.realNameAuthLogMapper.updateByExampleSelective(ulog, example);
        }
        return Result.success();
    }

    /**
     * 获取企业认证状态updateAuthStatus
     *
     * @return
     */
    public Result getMemeberAuthInfo() {
        Long id = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        Example e = new Example(RealNameAuthLog.class);
        e.createCriteria().andCondition(" member_id=" + id);
        List<RealNameAuthLog> realNameAuthLogs = realNameAuthLogMapper.selectByExample(e);
        if (CollectionUtils.isEmpty(realNameAuthLogs)) {
            return Result.failure(ResultCode.ACCOUNT_NOT_AUTH);
        } else {
            if (realNameAuthLogs.get(0).getStatus() == 1) {
                return Result.success();
            } else {
                return Result.failure(ResultCode.ACCOUNT_NOT_AUTH);
            }
        }
    }

    /**
     * 校验用户名是否可用
     *
     * @return
     */
    public Result checkData(String username) {
        Member record = new Member();
        record.setUsername(username);
        if (this.memberMapper.selectCount(record) != 0) {
            return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
        }
        return Result.success();
    }


    /**
     * 查询手机号是否已注册
     *
     * @param phone
     * @return
     */
    private boolean queryMemberByPhone(String phone) {
        Map<String, Object> stringObjectMap = memberMapper.selectMemberByPhone(phone);
        if (stringObjectMap == null) {
            return true;
        }
        return false;
    }


    /**
     * 用户提交实名认证申请
     *
     * @param member
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result applyForRealNameAuth(Member member) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        log.info("用户提交实名认证申请" + member);
        Example e = new Example(RealNameAuthLog.class);
        e.createCriteria().andCondition(" member_id=" + memberId);
        List<RealNameAuthLog> realNameAuthLogs = realNameAuthLogMapper.selectByExample(e);

        //认证成功 第一次提交
        if (CollectionUtils.isEmpty(realNameAuthLogs)) {
            RealNameAuthLog realNameAuthLog = new RealNameAuthLog();
            realNameAuthLog.setMemberId(memberId);
            realNameAuthLog.setStatus(0);
            realNameAuthLog.setCreateTime(new Date());
            realNameAuthLogMapper.insertSelective(realNameAuthLog);
        } else {  //第n次提交
            RealNameAuthLog realNameAuthLog = realNameAuthLogs.get(0);
            //已认证不允许重复认证
            if (realNameAuthLog.getStatus() == 1) {
                return Result.failure(ResultCode.ACCOUNT_HAS);
            }
            realNameAuthLog.setStatus(0);
            realNameAuthLogMapper.updateByPrimaryKeySelective(realNameAuthLog);
        }
        member.setId(memberId);
        this.memberMapper.updateByPrimaryKeySelective(member);
        return Result.success();
    }


    /**
     * 忘记密码修改密码
     *
     * @param memberDto
     * @return
     */
    public Result forgetPassword(MemberDto memberDto) {
        String phone = memberDto.getPhone();
        String password = memberDto.getPassword();
        String code = memberDto.getCode();
        if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(password) && StringUtils.isEmpty(code)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        Member member = new Member();
        // 生成盐
        String salt = Md5Utils.generate();
        member.setPassword(Md5Utils.encryptPassword(password, salt));
        member.setSalt(salt);
        Example example = new Example(Member.class);

        //判断是手机号还是邮箱
        boolean email = LoginUtils.isEmail(phone);
        boolean phoneNumberValid = LoginUtils.isPhoneNumberValid(phone);
        if (!email && !phoneNumberValid) {
            return Result.failure(ResultCode.PHONE_EMAIL_FORMAT_ERROR);
        }
        if (email) {
            //判断验证码是否正确
            String s = redisTemplate.opsForValue().get(KeyConstant.FORGET_CODE_EMAIL_KEY + phone);
            if (StringUtils.isEmpty(s)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            if (!s.equals(code)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            example.createCriteria().andCondition(" email = " + phone);
        }
        if (phoneNumberValid) {
            //判断验证码是否正确
            String s = redisTemplate.opsForValue().get(KeyConstant.FORGET_CODE_PHONE_KEY + phone);
            if (StringUtils.isEmpty(s)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            if (!code.equals(s)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }
            example.createCriteria().andCondition(" phone = " + phone);
        }
        int i = memberMapper.updateByExampleSelective(member, example);
        if (i > 0) {
            return Result.success();
        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }


    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    public Result updatePassword(Map<String, String> map) {
        Claims claims = LoginInterceptor.getLoginClaims();
        Long id = ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID));
        //如果是用户
        if (RoleConstant.MEMBER.equals(claims.get("roles"))) {
            Member m = new Member();
            m.setId(id);
            Member member = memberMapper.selectByPrimaryKey(m);

            m.setPassword(Md5Utils.encryptPassword(map.get("oldPassword"), member.getSalt()));
            int i = memberMapper.selectCount(m);
            if (i != 1) {
                return Result.failure(ResultCode.USER_OLD_PASSWORD_ERROR);
            }

            m.setPassword(Md5Utils.encryptPassword(map.get("newPassword"), member.getSalt()));
            memberMapper.updateByPrimaryKeySelective(m);
        } else { //管理员
            User user = new User();
            user.setId(id);
            user.setPassword(map.get("newPassword"));
            userMapper.updateByPrimaryKeySelective(user);
        }
        return Result.success();
    }

    /**
     * 查询用户账号绑定状态
     *
     * @return
     */
    public Result getMyAccountInfo() {
        Claims claims = LoginInterceptor.getLoginClaims();
        Long id = ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID));
        Member m = new Member();
        m.setId(id);
        Member member = memberMapper.selectByPrimaryKey(m);
        return Result.success(member);
    }

    /**
     * 绑定手机/邮箱
     *
     * @param type  类型
     * @param param 邮箱
     * @param code  验证码
     * @return 绑定是否成功
     */
    public Result bind(String type, String param, String code) {
        Claims claims = LoginInterceptor.getLoginClaims();
        Long id = ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID));
        if ("email".equals(type)) {
            //判断邮箱格式是否正确
            boolean email = LoginUtils.isEmail(param);
            if (!email) {
                return Result.failure(ResultCode.EMAIL_NULL_ERROR);
            }
            //判断邮箱是否已注册
            Integer i = this.memberMapper.selectMemberByEmail(param);
            if (i > 0) {
                return Result.failure(ResultCode.EMAIL_HAS_REGISTER);
            }
            String s = this.redisTemplate.opsForValue().get(KeyConstant.MEMBER_BIND_EMAIL_KEY + param);
            if (!code.equals(s)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }

            //修改用户邮箱
            Member m = new Member();
            m.setId(id);
            m.setEmail(param);
            memberMapper.updateByPrimaryKeySelective(m);
            m = memberMapper.selectByPrimaryKey(m);
            return Result.success(m);
        } else if ("phone".equals(type)) {
            //判断手机格式是否正确
            boolean phone = LoginUtils.isPhoneNumberValid(param);
            if (!phone) {
                return Result.failure(ResultCode.PHONE_FORMAT_ERROR);
            }
            //判断shouji是否已注册
            Integer i = this.memberMapper.selectLoginMemberByPhone(param);
            if (i > 0) {
                return Result.failure(ResultCode.PHONE_HAS_REGISTER);
            }
            String s = this.redisTemplate.opsForValue().get(KeyConstant.MEMBER_BIND_PHONE_KEY + param);
            if (!code.equals(s)) {
                return Result.failure(ResultCode.CODE_ERROR);
            }

            Member m = new Member();
            m.setId(id);
            m.setPhone(param);
            memberMapper.updateByPrimaryKeySelective(m);
            m = memberMapper.selectByPrimaryKey(m);
            return Result.success(m);
        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }

    /**
     * 初始化密码
     *
     * @param member
     * @return
     */
    public Result initializationPwd(Member member) {
        String salt = Md5Utils.generate();
        member.setPassword(Md5Utils.encryptPassword(AesEncrypt.decryptAES(member.getPassword()), salt));
        member.setSalt(salt);
        boolean boo = this.memberMapper.updateByPrimaryKeySelective(member) == 1;
        if (boo) {
            // TODO: 2019/11/10 需要添加短息通知
            return Result.success(ResultCode.DATA_FOUND_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_FOUND_ERROR);
    }


    /**
     * 确认开通
     *
     * @param member 用户信息
     * @return 开通成功/失败
     */
    public Result isOpen(Member member) {
        boolean boo = this.memberMapper.updateByPrimaryKeySelective(member) == 1;
        if (boo) {
            // TODO: 2019/11/10 需要添加短息通知
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

}
