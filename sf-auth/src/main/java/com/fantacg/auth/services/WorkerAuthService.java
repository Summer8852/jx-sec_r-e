package com.fantacg.auth.services;

import com.alibaba.fastjson.JSON;
import com.fantacg.auth.client.UserClient;
import com.fantacg.auth.properties.JwtProperties;
import com.fantacg.common.auth.utils.JwtUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RTypeCostant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.Worker;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.CookieUtils;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author SUMMER
 */
@Service
@Slf4j
public class WorkerAuthService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserClient userClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtProperties properties;
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpServletRequest request;

    /**
     * 工人登录
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result workerLogin(MemberDto memberDto) {
        try {
            Result result = new Result();
            HashMap<String, Object> map = new HashMap<>();
            //判断参数不能为空
            if (StringUtils.isEmpty(memberDto.getLoginType())) {
                return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
            }

            //判断登陆类型 （LOGIN_ONE 手机和密码登录 LOGIN_TWO手机和验证码登录）
            if (RTypeCostant.LOGIN_TWO.equals(memberDto.getLoginType())) {
                //判断手机号和验证码是否为空
                if (StringUtils.isEmpty(memberDto.getPhone()) || StringUtils.isEmpty(memberDto.getCode())) {
                    return Result.failure(ResultCode.PHONE_CODE_NULL_ERROR);
                }

                // 从redis取出验证码 判断验证码是否正确
                String key = KeyConstant.WORKER_LOGIN_CODE_KEY_PREFIX + AesEncrypt.decryptAES(memberDto.getPhone());
                String codeCache = this.redisTemplate.opsForValue().get(key);
                if (!memberDto.getCode().equals(codeCache)) {
                    return Result.failure(ResultCode.CODE_ERROR);
                }

                // 调用微服务，执行查询获取用户信息
                result = this.userClient.loginWorkerByPhone(AesEncrypt.decryptAES(memberDto.getPhone()));
            } else if (RTypeCostant.LOGIN_ONE.equals(memberDto.getLoginType())) {
                // 调用微服务，执行查询
                result = this.userClient.workerByPhoneOrPwd(AesEncrypt.decryptAES(memberDto.getPhone()), AesEncrypt.decryptAES(memberDto.getPassword()));
            }

            if (!result.getCode().equals(ResultCode.SUCCESS.code())) {
                return result;
            }

            Worker worker = JSON.parseObject(JSON.toJSONString(result.getData()), Worker.class);
            // 如果有查询结果，则生成token
            String token = new JwtUtils().createJWT(worker.getId().toString(), worker.getPhone(), RoleConstant.WORKER,
                    properties.getPrivateKey(), properties.getExpire());
            String IdCard = "";
            if (StringUtil.isNotEmpty(worker.getIdCard())) {
                IdCard = AesEncrypt.encryptAES(worker.getIdCard());
            }
            // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
            CookieUtils.newBuilder(response).httpOnly().maxAge(properties.getCookieMaxAge()).request(request).build(properties.getCookieName(), token);
            map.put("cookieName", properties.getCookieName());
            map.put("cookie", token);
            map.put("auth", worker.getIsAuth());
            map.put("cardNum", IdCard);
            return Result.success(map);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }
}
