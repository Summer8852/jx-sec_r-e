package com.fantacg.user.service;

import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.user.User;
import com.fantacg.common.pojo.user.UserRole;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.Md5Utils;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.mapper.UserMapper;
import com.fantacg.user.mapper.UserRoleMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname UserService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    /**
     * 平台管理员注册的方法
     *
     * @param user
     * @return
     */
    public Boolean register(User user) {
        log.info("用户注册" + user);
        user.setId(null);
        user.setCreateTime(new Date());
        // 生成盐
        String salt = Md5Utils.generate();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(), salt));
        // 写入数据库
        boolean boo = this.userMapper.insertSelective(user) == 1;
        return boo;
    }


    /**
     * 根据用户名和密码查询用户
     *
     * @param memberDto
     * @return
     */
    public Result loginUser(MemberDto memberDto) {
        log.info("内部管理员登录");
        String account = ""; String password = "";
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
        User user = new User();
        user.setUsername(account);
        user = this.userMapper.selectOne(user);
        // 判断管理员账号是否存在
        if (user == null) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
        }

        //判断密码是否正确
        if (!user.getPassword().equals(Md5Utils.encryptPassword(password, user.getSalt()))) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_PWD_ERROR);
        }
        // 用户名密码都正确
        return Result.success(user);
    }

    /**
     * 查询所有用户
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public Result queryUserByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(User.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("username", "%" + key + "%")
                    .orLike("phone", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<User> pageInfo = (Page<User>) userMapper.selectByExample(example);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 根据id删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        Example example = new Example(UserRole.class);
        example.createCriteria().andCondition(" user_id =" + id);
        userRoleMapper.deleteByExample(example);
        userMapper.deleteByPrimaryKey(id);

        return Result.success(ResultCode.DATA_DELETE_SUCCESS);

    }

    /**
     * 根据Id查询用户
     */
    public Result queryUserById(Long id) {
        return Result.success(userMapper.selectByPrimaryKey(id));
    }

    /**
     * 修改用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Result editUser(User user) {
        //不允许修改username
        user.setUsername(null);
        //修改密码先去获取盐
        if (StringUtils.isNotBlank(user.getPassword())) {
            User user1 = userMapper.selectByPrimaryKey(user.getId());
            user.setPassword(Md5Utils.encryptPassword(user.getPassword(), user1.getSalt()));
            user.setEmail(user.getEmail());
        }
        int i = this.userMapper.updateByPrimaryKeySelective(user);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 添加管理员
     *
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addUser(User user) {
        User u = new User();
        u.setUsername(user.getUsername());
        int count = userMapper.selectCount(u);
        if (count > 0) {
            return Result.failure("用户名已存在");
        }
        user.setId(null);
        user.setCreateTime(new Date());
        // 生成盐
        String salt = Md5Utils.generate();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(), salt));
        // 写入数据库
        this.userMapper.insertSelective(user);

        //新增用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(2L);
        count = userRoleMapper.insertSelective(userRole);
        if (count > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }
}
