package com.fantacg.auth.services;

import com.alibaba.fastjson.JSON;
import com.fantacg.auth.client.UserClient;
import com.fantacg.auth.properties.JwtProperties;
import com.fantacg.common.auth.utils.JwtUtils;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.User;
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
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SUMMER
 */
@Service
@Slf4j
public class UserAuthService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserClient userClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtProperties properties;
    @Autowired
    HttpServletRequest request;


    /**
     * 调用微服务，执行查询:根据用户名和密码查询用户
     *
     * @param memberDto 账号
     * @return
     */
    public Result authentication(MemberDto memberDto, HttpServletResponse response) {

        try {
            // 远程调用服务查询数据
            Result result = this.userClient.loginUser(memberDto);

            if (!result.getCode().equals(ResultCode.SUCCESS.code())) {
                return result;
            }
            // JSON 解析成对象
            User user = objectMapper.readValue(JSON.toJSONString(result.getData()), User.class);
            // 用户信息生成token
            String token = new JwtUtils().createJWT(user.getId().toString(), user.getUsername(), RoleConstant.USER, properties.getPrivateKey(), properties.getExpire());
            // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
            CookieUtils.newBuilder(response).httpOnly().maxAge(properties.getCookieMaxAge()).request(request).build(properties.getCookieName(), token);
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
            map.put("cookieName", properties.getCookieName());
            map.put("cookie", token);
            return Result.success(map);

        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

}
