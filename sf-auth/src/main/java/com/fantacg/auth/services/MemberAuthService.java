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
import com.fantacg.common.pojo.user.Member;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SUMMER
 */
@Service
@Slf4j
public class MemberAuthService {

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
     * 用户注册
     *
     * @param memberDto
     * @return
     */
    public Result registerMember(MemberDto memberDto, HttpServletResponse response) {
        log.debug("注册：" + memberDto);
        try {
            // 调用微服务，执行查询
            Result result = this.userClient.registerMember(memberDto);
            if (result.getCode().equals(ResultCode.SUCCESS.code())) {
                Long memberId = (Long) result.getData();
                String token = null;
                // 手机号+验证码登录
                if (RTypeCostant.REGISTER_TYPE_ONE.equals(memberDto.getRegisterType())) {
                    // 如果有查询结果，则生成token
                    token = new JwtUtils().createJWT(memberId.toString(), memberDto.getPhone(), RoleConstant.MEMBER,
                            properties.getPrivateKey(), properties.getExpire());
                }
                // 手机号+密码登录
                if (RTypeCostant.REGISTER_TYPE_TWO.equals(memberDto.getRegisterType())) {
                    // 如果有查询结果，则生成token
                    token = new JwtUtils().createJWT(memberId.toString(), memberDto.getPhone(), RoleConstant.MEMBER,
                            properties.getPrivateKey(), properties.getExpire());
                }
                // 邮箱+验证链接+密码注册
                if (RTypeCostant.REGISTER_TYPE_THREE.equals(memberDto.getRegisterType())) {
                    token = new JwtUtils().createJWT(memberId.toString(), memberDto.getEmail(), RoleConstant.MEMBER,
                            properties.getPrivateKey(), properties.getExpire());
                }
                // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
                CookieUtils.newBuilder(response).httpOnly().maxAge(properties.getCookieMaxAge()).request(request).build(properties.getCookieName(), token);
                log.debug("用户注册成功");
                return Result.success(token);

            }
            return result;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 用户登录
     *
     * @return
     */
    public Result memberLogin(MemberDto memberDto, HttpServletResponse response) {
        try {
            Result result = new Result();
            // 判断 密码、验证码 登录
            if (RTypeCostant.LOGIN_TWO.equals(memberDto.getLoginType())) {
                result = this.userClient.loginMemberByPhone(memberDto);
            } else if (RTypeCostant.LOGIN_ONE.equals(memberDto.getLoginType())) {
                result = this.userClient.loginMember(memberDto);
            }

            System.out.println(result);

            // 返回200 说明成功
            if (!result.getCode().equals(ResultCode.SUCCESS.code())) {
                return result;
            }

            // 数据不为NULL 使用JSON 解析对象
            String s = JSON.toJSONString(result.getData());
            Member member = objectMapper.readValue(s, Member.class);

            // 如果有查询结果，则生成token
            String token = new JwtUtils().createJWT(member.getId().toString(), member.getUsername(), RoleConstant.MEMBER,
                    properties.getPrivateKey(), properties.getExpire());

            // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
            CookieUtils.newBuilder(response).httpOnly().maxAge(properties.getCookieMaxAge()).request(request).build(properties.getCookieName(), token);
            ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
            map.put("cookieName", properties.getCookieName());
            map.put("cookie", token);
            return Result.success(map);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


}
