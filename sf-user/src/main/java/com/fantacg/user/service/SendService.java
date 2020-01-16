package com.fantacg.user.service;

import cn.hutool.core.util.RandomUtil;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RTypeCostant;
import com.fantacg.common.constant.RpcConstant;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.Member;
import com.fantacg.common.pojo.user.Worker;
import com.fantacg.common.utils.*;
import com.fantacg.user.config.CommonRpc;
import com.fantacg.user.mapper.MemberMapper;
import com.fantacg.user.mapper.WorkerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname SendService 发送短信或邮箱Service
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class SendService {


    @Value("${spring.mail.username}")
    String username;

    @Value("${spring.mail.subject}")
    String subject;

    @Value("${spring.mail.emailUel}")
    String emailUel;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    MailService mailService;

    @Autowired
    WorkerMapper workerMapper;


    /**
     * 注册发送短信的方法
     *
     * @param phone
     */
    public Result sendVerifyCode(String phone) {
        try {
            Long expire = redisTemplate.getExpire(KeyConstant.MEMBER_REGISTER_CODE_KEY_PREFIX + phone);
            if (expire > 60 * 4) {
                return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
            }

            Map<String, Object> stringObjectMap = memberMapper.selectMemberByPhone(phone);

            if (null != stringObjectMap) {
                return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
            }

            // 生成验证码
            String code = RandomUtil.randomNumbers(6);
            // 发送短信
            ConcurrentHashMap<String, Object> msg = new ConcurrentHashMap<>();
            msg.put("code", code);
            // TODO: 2019/3/14 需要添加 发送短信
            boolean sms = CommonRpc.sms(phone, RpcConstant.REGISTER_CODE, msg);
            if (sms) {
                // 将code存入redis
                this.redisTemplate.opsForValue().set(KeyConstant.MEMBER_REGISTER_CODE_KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
                return Result.success();
            }
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param memberDto
     * @return
     */
    public Result sendMail(MemberDto memberDto) {

        if (StringUtils.isEmpty(memberDto.getEmail()) && StringUtils.isEmpty(memberDto.getRegisterType())) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }

        //判断邮箱格式是否正确
        boolean email = LoginUtils.isEmail(memberDto.getEmail());
        if (!email) {
            return Result.failure(ResultCode.EMAIL_NULL_ERROR);
        }

        //判断邮箱是否已注册
        int i = this.memberMapper.selectMemberByEmail(memberDto.getEmail());
        if (i > 0) {
            return Result.failure(ResultCode.EMAIL_HAS_REGISTER);
        }

        // 3 邮箱 + 验证链接 + 密码
        if (!RTypeCostant.REGISTER_TYPE_THREE.equals(memberDto.getRegisterType())) {
            return Result.failure(ResultCode.PARAMETER_ERROR);
        }

        //一分钟内只发送一条
        Long expire = redisTemplate.getExpire(KeyConstant.MEMBER_CODE_EMAIL_KEY + memberDto.getEmail());
        if (expire > 60 * 4) {
            return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
        }

        // 生成验证码
        String code = RandomUtil.randomNumbers(6);

        //发送邮箱 内容 验证码 和 链接(点击链接跳转页面可 填写密码)
        String html = "<html><body>"
                + " <div> "
                + "   <b>注册验证码:</b><a>" + code + "</a></br>"
                + "   <h3>点击下方链接即可注册</h3></br>" + emailUel + "?email=" + memberDto.getEmail()
                + "</div>"
                + "</body></html>";
        boolean b = mailService.sendTemplateMail(username, memberDto.getEmail(),
                subject, html);
        if (b) {
            // 将code存入redis
            this.redisTemplate.opsForValue().set(KeyConstant.MEMBER_CODE_EMAIL_KEY + memberDto.getEmail(), code, 5, TimeUnit.MINUTES);
            return Result.success();
        }
        return Result.failure(ResultCode.EMAIL_NULL_ERROR);
    }

    /**
     * 登录获取验证码
     *
     * @param phone
     * @return
     */
    public Result sendLoginCode(String phone) {
        try {
            Long expire = redisTemplate.getExpire(KeyConstant.MEMBER_LOGIN_CODE_KEY_PREFIX + phone);
            if (expire > 60 * 4) {
                return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
            }

            int i = memberMapper.selectLoginMemberByPhone(phone);

            if (i == 0) {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
            }

            // 生成验证码
            String code = RandomUtil.randomNumbers(6);
            // 发送短信
            ConcurrentHashMap<String, Object> msg = new ConcurrentHashMap<>();
            msg.put("code", code);
            // TODO: 2019/3/14 需要添加 发送短信
            boolean sms = CommonRpc.sms(phone, RpcConstant.LOGIN_CODE, msg);
            if (sms) {
                // 将code存入redis
                this.redisTemplate.opsForValue().set(KeyConstant.MEMBER_LOGIN_CODE_KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
                return Result.success();
            }
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 发送验证码
     */
    public Result forgetCode(String phone) {
        //判断是手机号还是邮箱
        boolean email = LoginUtils.isEmail(phone);
        boolean phoneNumberValid = LoginUtils.isPhoneNumberValid(phone);
        if (!email && !phoneNumberValid) {
            return Result.failure(ResultCode.PHONE_EMAIL_FORMAT_ERROR);
        }
        Member member = new Member();
        //生成验证码
        String code = RandomUtil.randomNumbers(6);
        if (email) {
            member.setEmail(phone);
            int i = memberMapper.selectCount(member);
            if (i > 0) {
                //发送邮箱验证码
                Long expire = redisTemplate.getExpire(KeyConstant.FORGET_CODE_EMAIL_KEY + phone);
                if (expire > 60 * 4) {
                    return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
                }
                //发送邮箱 内容 验证码 和 链接(点击链接跳转页面可 填写密码)
                String html = "<html><body>"
                        + " <div> "
                        + "   <b>忘记验证码:</b><a>" + code + "</a>"
                        + "</div>"
                        + "</body></html>";
                boolean b = mailService.sendTemplateMail(username, phone,
                        subject, html);
                if (b) {
                    // 将code存入redis
                    this.redisTemplate.opsForValue().set(KeyConstant.FORGET_CODE_EMAIL_KEY + phone, code, 5, TimeUnit.MINUTES);
                    return Result.success();
                }
            } else {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
            }
        }
        if (phoneNumberValid) {
            member.setPhone(phone);
            int i = memberMapper.selectCount(member);
            if (i > 0) {
                //发送手机验证码
                Long expire = redisTemplate.getExpire(KeyConstant.FORGET_CODE_PHONE_KEY + phone);
                if (expire > 60 * 4) {
                    return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
                }
                // 发送短信
                ConcurrentHashMap<String, Object> msg = new ConcurrentHashMap<>();
                msg.put("code", code);
                // TODO: 2019/3/14  发送短信
                boolean sms = CommonRpc.sms(phone, RpcConstant.LOGIN_CODE, msg);
                if (sms) {
                    // 将code存入redis
                    this.redisTemplate.opsForValue().set(KeyConstant.FORGET_CODE_PHONE_KEY + phone, code, 5, TimeUnit.MINUTES);
                    return Result.success();
                }
            } else {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
            }
        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }

    /**
     * 发送绑定验证码
     *
     * @param type  类型
     * @param param 邮箱/手机号
     * @return
     */
    public Result sendBindCode(String type, String param) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(6);
        if ("email".equals(type)) {
            Boolean e = LoginUtils.isEmail(param);
            if (!e) {
                return Result.failure(ResultCode.EMAIL_NULL_ERROR);
            }
            //判断邮箱是否已注册
            Integer i = this.memberMapper.selectMemberByEmail(param);
            if (i > 0) {
                return Result.failure(ResultCode.EMAIL_HAS_REGISTER);
            }

            Boolean b = mailService.sendSimpleMail(username, param, subject, code);
            if (b) {
                // 将code存入redis
                this.redisTemplate.opsForValue().set(KeyConstant.MEMBER_BIND_EMAIL_KEY + param, code, 5, TimeUnit.MINUTES);
                return Result.success();
            }
        } else if ("phone".equals(type)) {
            boolean e = LoginUtils.isPhoneNumberValid(param);
            if (!e) {
                return Result.failure(ResultCode.PHONE_FORMAT_ERROR);
            }
            //判断shouji是否已注册
            Integer i = this.memberMapper.selectLoginMemberByPhone(param);
            if (i > 0) {
                return Result.failure(ResultCode.PHONE_HAS_REGISTER);
            }

            //判断短信验证码是否发送频繁
            Long expire = this.redisTemplate.getExpire(KeyConstant.MEMBER_BIND_PHONE_KEY + param);
            if (expire > 60 * 4) {
                return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
            }

            ConcurrentHashMap<String, Object> cmap = new ConcurrentHashMap<>();
            cmap.put("code", code);
            boolean sms = CommonRpc.sms(param, RpcConstant.LOGIN_CODE, cmap);
            if (sms) {
                // 将code存入redis
                this.redisTemplate.opsForValue().set(KeyConstant.MEMBER_BIND_PHONE_KEY + param, code, 5, TimeUnit.MINUTES);
                return Result.success();
            }

        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }


    /**
     * 注册发送短信的方法
     *
     * @param phone
     */
    public Result sendWorkerVerifyCode(String phone) {
        try {
            //判断手机号码是否正确

            Long expire = redisTemplate.getExpire(KeyConstant.WORKER_REGISTER_CODE_KEY_PREFIX + phone);
            if (expire > 60 * 4) {
                return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
            }

            Worker worker = new Worker();
            worker.setPhone(phone);
            int i = workerMapper.selectCount(worker);

            if (i > 0) {
                return Result.failure(ResultCode.LOGIN_PHONE_EXIST_ERROR);
            }

            // 生成验证码
            String code = RandomUtil.randomNumbers(6);
            // 发送短信
            ConcurrentHashMap<String, Object> msg = new ConcurrentHashMap<>();
            msg.put("code", code);
            // TODO: 2019/3/14 需要添加 发送短信
            boolean sms = CommonRpc.sms(phone, RpcConstant.REGISTER_CODE, msg);
            System.out.println(sms);
            if (sms) {
                // 将code存入redis
                this.redisTemplate.opsForValue().set(KeyConstant.WORKER_REGISTER_CODE_KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
                return Result.success();
            } else {
                return Result.failure(ResultCode.PHONE_FORMAT_ERROR);
            }
        } catch (Exception e) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 工人登录获取验证码
     *
     * @param phone
     * @return
     */
    public Result sendWorkerLoginCode(String phone) {
        try {

            Long expire = redisTemplate.getExpire(KeyConstant.WORKER_LOGIN_CODE_KEY_PREFIX + AesEncrypt.decryptAES(phone));
            if (expire > 60 * 4) {
                return Result.failure(ResultCode.CODE_SMS_NOT_LOSE_EFFICACY);
            }

            Worker worker = new Worker();
            worker.setPhone(AesEncrypt.decryptAES(phone));
            int i = workerMapper.selectCount(worker);

            if (i == 0) {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
            }

            // 生成验证码
            String code = RandomUtil.randomNumbers(6);
            // 发送短信
            ConcurrentHashMap<String, Object> msg = new ConcurrentHashMap<>();
            msg.put("code", code);
            // TODO: 2019/3/14 需要添加 发送短信
            boolean sms = CommonRpc.sms(AesEncrypt.decryptAES(phone), RpcConstant.LOGIN_CODE, msg);
            if (sms) {
                // 将code存入redis
                this.redisTemplate.opsForValue().set(KeyConstant.WORKER_LOGIN_CODE_KEY_PREFIX + AesEncrypt.decryptAES(phone), code, 5, TimeUnit.MINUTES);
                return Result.success();
            }
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

}
