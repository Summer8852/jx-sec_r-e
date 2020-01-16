package com.fantacg.auth.controller;

import cn.hutool.core.date.DateUtil;
import com.fantacg.auth.properties.JwtProperties;
import com.fantacg.auth.services.MemberAuthService;
import com.fantacg.auth.services.UserAuthService;
import com.fantacg.auth.services.WorkerAuthService;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.JwtUtils;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.CookieUtils;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname AuthController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    MemberAuthService memberAuthService;
    @Autowired
    UserAuthService userAuthService;
    @Autowired
    WorkerAuthService workerAuthService;
    @Autowired
    JwtProperties properties;
    @Autowired
    StringRedisTemplate redisTemplate;


    /**
     * 系统管理员登录授权
     *
     * @param
     * @return
     */
    @PostMapping("/login")
    public Result login(
            @RequestBody MemberDto memberDto, HttpServletResponse response) {
        return this.userAuthService.authentication(memberDto,response);
    }

    /**
     * 用户登录授权
     *
     * @param memberDto
     * @param response
     * @return
     */
    @PostMapping("/member/login")
    public Result memberLogin(
            @RequestBody MemberDto memberDto,
            HttpServletResponse response) {
        return this.memberAuthService.memberLogin(memberDto, response);
    }


    /**
     * 工人登录授权
     * @param
     * @return
     */
    @PostMapping("/wl")
    public Result workerLogin(
            @RequestBody MemberDto memberDto) {
        return this.workerAuthService.workerLogin(memberDto);
    }


    /**
     * 验证用户信息,刷新cookie有效期
     *
     * @param token
     * @return
     */
    @GetMapping("/verify")
    @ResponseBody
    public Result verifyUser(@CookieValue("SF_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) {
        Claims claims = new JwtUtils().parseJWT(this.properties.getPublicKey(), token);
        String newToken = null;
        String roles = null;
        if (claims != null) {
            if (claims.get("roles").equals(RoleConstant.USER)) {
                newToken = new JwtUtils().createJWT(ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_ID)), ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_USER_NAME)), RoleConstant.USER, this.properties.getPrivateKey(), this.properties.getExpire());
                roles = RoleConstant.USER;
            } else {
                newToken = new JwtUtils().createJWT(ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_ID)), ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_USER_NAME)), RoleConstant.MEMBER, this.properties.getPrivateKey(), this.properties.getExpire());
                roles = RoleConstant.MEMBER;
            }

            // 更新cookie中的token
            CookieUtils.newBuilder(response).httpOnly().maxAge(this.properties.getCookieMaxAge()).request(request).build(this.properties.getCookieName(), newToken);
            // 解析成功返回用户信息
            ConcurrentHashMap map = new ConcurrentHashMap();
            map.put("cookieName", properties.getCookieName());
            map.put("cookie", newToken);
            map.put("roles", roles);
            return Result.success(map);
        }
        return Result.failure(ResultCode.LOGIN_TOKEN_EXPIRE_ERROR);
    }


    /**
     * 退出登录
     *
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    @ResponseBody
    public Result logout(@CookieValue("SF_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) {
        // 从token中解析token信息
        Claims claims = new JwtUtils().parseJWT(this.properties.getPublicKey(), token);
        if (claims != null && claims.get("roles").equals(RoleConstant.MEMBER)) {
            String key = KeyConstant.PERMISS_KEY_PREFIX + ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID));
            redisTemplate.delete(key);
        }
        //清除cookie
        CookieUtils.newBuilder(response).httpOnly().maxAge(0).request(request).build(this.properties.getCookieName(), "");
        return Result.success();
    }


    /**
     * qq登录 发送请求到腾讯服务器
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/qqLogin")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /************************************ 注册 *************************************/

    /***
     * 用户账号注册
     * @param memberDto
     * @return
     */
    @PostMapping("/registerMember")
    @ResponseBody
    public Result registerMember(
            @RequestBody MemberDto memberDto,
            HttpServletResponse response) {
        return this.memberAuthService.registerMember(memberDto, response);
    }

    /******************************** 第三方登录 **********************************/

    /**
     * QQ回调方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/connect")
    public void connect(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html; charset=utf-8");

        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

            String accessToken = null,
                    openID = null;
            long tokenExpireIn = 0L;


            if ("".equals(accessTokenObj.getAccessToken())) {
//                我们的网站被CSRF攻击了或者用户取消了授权
//                做一些数据统计工作
            } else {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();

                request.getSession().setAttribute("demo_access_token", accessToken);
                request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();

                //out.println("欢迎你，代号为 " + openID + " 的用户!");
                request.getSession().setAttribute("demo_openid", openID);


                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
//                if (userInfoBean.getRet() == 0) {
//                    out.println(userInfoBean.getNickname() + "<br/>");
//                    out.println(userInfoBean.getGender() + "<br/>");
//
//                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL30() + "><br/>");
//                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL50() + "><br/>");
//                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL100() + "><br/>");
//                } else {
//                    out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
//                }


            }
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

}
