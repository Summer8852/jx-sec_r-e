package com.fantacg.user.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.MQConstant;
import com.fantacg.common.constant.RTypeCostant;
import com.fantacg.common.dto.user.WorkerDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.Member;
import com.fantacg.common.pojo.user.Worker;
import com.fantacg.common.pojo.worker.WorkerInfo;
import com.fantacg.common.utils.*;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.WorkerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname WorkerService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Service
public class WorkerService {

    @Autowired
    WorkerMapper workerMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 工人注册
     *
     * @param worker
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result workerRegister(Worker worker) {
        log.info("工人注册:" + worker);
        String registerType = worker.getRegisterType();
        String phone = worker.getPhone();
        String password = worker.getPassword();
        String code = worker.getCode();
        int i = 0;
        int count = 0;
        //判断手机号是否存在
        if (StringUtil.isEmpty(phone)) {
            return Result.failure(ResultCode.PHONE_CODE_NULL_ERROR);
        }
        Example example = new Example(Worker.class);
        example.createCriteria().andCondition(" phone = " + AesEncrypt.decryptAES(phone));
        count = workerMapper.selectCountByExample(example);


        if (count == 0) {
            //REGISTER_TYPE_ONE : 手机号+验证码
            //REGISTER_TYPE_TWO ：手机号+密码
            if (RTypeCostant.REGISTER_TYPE_ONE.equals(registerType)) {
                //判断手机号 和验证码是否为空
                if (StringUtil.isEmpty(code)) {
                    return Result.failure(ResultCode.PHONE_CODE_NULL_ERROR);
                }
                // 从redis取出验证码
                String codeCache = this.redisTemplate.opsForValue().get(KeyConstant.WORKER_REGISTER_CODE_KEY_PREFIX + AesEncrypt.decryptAES(phone));
                //判断验证码是否过期 或验证码是否正确
                if (StringUtils.isNotEmpty(codeCache) && code.equals(codeCache)) {
                    worker.setId(new IdWorker().nextId());
                    worker.setWorkerName(AesEncrypt.decryptAES(phone));
                    worker.setPhone(AesEncrypt.decryptAES(phone));
                    worker.setCreateTime(new Date());
                    worker.setIsAuth(0);
                    i = this.workerMapper.insertSelective(worker);
                } else {
                    return Result.failure(ResultCode.CODE_ERROR);
                }

            } else if (RTypeCostant.REGISTER_TYPE_TWO.equals(registerType)) {
                //判断手机号 和 密码是否为空
                if (StringUtil.isEmpty(password)) {
                    return Result.failure(ResultCode.PHONE_PWD_NULL_ERROR);
                }

                // 生成盐
                String salt = Md5Utils.generate();
                // 对密码进行加密
                worker.setId(new IdWorker().nextId());
                worker.setWorkerName(AesEncrypt.decryptAES(phone));
                worker.setPhone(AesEncrypt.decryptAES(phone));
                worker.setPassword(Md5Utils.encryptPassword(AesEncrypt.decryptAES(password), salt));
                worker.setSalt(salt);
                worker.setIsAuth(0);
                worker.setCreateTime(new Date());
                i = this.workerMapper.insertSelective(worker);
            }

            if (i > 0) {
                return Result.success(ResultCode.SUCCESS);
            }
        } else {
            return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
        }
        return Result.failure(ResultCode.REGISTER_EXPIRE);
    }

    /**
     * 手机号查询工人信息
     *
     * @param map
     * @return
     */
    public Result workerByPhone(Map map) {
        log.info("手机号查询工人信息:" + map);
        String phone = (String) map.get("phone");
        if (StringUtil.isNotEmpty(phone)) {
            HashMap<String, Object> hashMap = this.workerMapper.workerByPhone(AesEncrypt.decryptAES(phone));
            return Result.success(hashMap);
        } else {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 手机号码 验证码 登录
     *
     * @param phone
     * @return
     */
    public Result loginWorkerByPhone(String phone) {
        log.info("手机号和验证码查询工人信息:" + phone);
        Worker worker = new Worker();
        worker.setPhone(phone);
        worker = this.workerMapper.selectOne(worker);
        if (worker == null) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
        }
        return Result.success(worker);
    }

    /**
     * 手机号和密码查询工人信息
     *
     * @param phone
     * @return
     */
    public Result workerByPhoneOrPwd(String phone, String password) {
        log.info("手机号和密码查询工人信息:" + phone + "----" + password);
        Worker worker = new Worker();
        worker.setPhone(phone);
        worker = this.workerMapper.selectOne(worker);
        // 校验用户名
        if (worker == null) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
        }
        // 校验密码
        if (!worker.getPassword().equals(Md5Utils.encryptPassword(password, worker.getSalt()))) {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_PWD_ERROR);
        }
        // 用户名密码都正确
        return Result.success(worker);
    }


    /**
     * 实人认证上传资料
     *
     * @param workerDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addWorker(WorkerDto workerDto) {
        log.info("实人认证上传资料:" + workerDto);
        Long id = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        log.info("工人id:" + id);
        if (workerDto != null) {
            Worker worker = new Worker();
            worker.setId(id);
            worker.setWorkerName(workerDto.getName());
            worker.setIdCard(workerDto.getIdCardNumber());
            worker.setRealNameImgFront(workerDto.getPositiveIdCardImageUrl());
            worker.setRealNameImgBack(workerDto.getNegativeIdCardImageUrl());
            worker.setRealNameImgPerson(workerDto.getHeadImageUrl());
            worker.setIsAuth(1);
            int i = this.workerMapper.updateWorkerById(worker);
            if (i > 0) {
                try {
                    WorkerInfo workerInfo = new WorkerInfo();
                    workerInfo.setName(workerDto.getName());
                    workerInfo.setIdCardType(workerDto.getIdCardType());
                    workerInfo.setIdCardNumber(workerDto.getIdCardNumber());
                    workerInfo.setGender(workerDto.getGender());
                    workerInfo.setNation(workerDto.getNation());
                    workerInfo.setBirthday(workerDto.getBirthday());
                    workerInfo.setAddress(workerDto.getAddress());
                    workerInfo.setHeadImageUrl(workerDto.getHeadImageUrl());
                    workerInfo.setGrantOrg(workerDto.getGrantOrg());
                    workerInfo.setPositiveIdCardImageUrl(workerDto.getPositiveIdCardImageUrl());
                    workerInfo.setNegativeIdCardImageUrl(workerDto.getNegativeIdCardImageUrl());
                    workerInfo.setStartDate(workerDto.getStartDate());
                    workerInfo.setExpiryDate(workerDto.getExpiryDate());
                    redisTemplate.convertAndSend("workerInfo", JSON.toJSONString(workerInfo));
                } catch (Exception e) {
                    log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
                    throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
                }
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            }
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 初始化密码
     *
     * @param
     * @return
     */
    public Result initializationPwd(Worker worker) {
        String salt = Md5Utils.generate();
        worker.setPassword(Md5Utils.encryptPassword("123456", salt));
        worker.setSalt(salt);
        boolean boo = this.workerMapper.updateByPrimaryKeySelective(worker) == 1;
        if (boo) {
            // TODO: 2019/11/10 待定是否需要添加短息通知
            return Result.success(ResultCode.DATA_FOUND_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_FOUND_ERROR);
    }

}
