package com.fantacg.auth.client;


import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.pojo.user.User;
import com.fantacg.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 智慧安全云
 * @Classname UserClient 管理员认证权限服务
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@FeignClient(value = "user-service")
public interface UserClient {

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/send")
    void sendVerifyCode(@RequestParam("phone") String phone);

    /**
     * 管理员注册
     *
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/register")
    void register(@Valid User user, @RequestParam("code") String code);

    /**
     * 根据用户名和密码查询用户
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/loginUser")
    Result loginUser(@RequestBody MemberDto memberDto);

    /**
     * 根据用户id查询权限列表
     *
     * @return
     */
    @GetMapping("/menu/doGetAuthorizationInfo")
    Result doGetAuthorizationInfo();

    /**
     * 获取用户认证信息
     *
     * @return
     */
    @GetMapping("/member/getMemeberAuthInfo")
    Result getMemeberAuthInfo();

    /**
     * 用户账号注册
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/member" + "/register")
    Result registerMember(@RequestBody MemberDto memberDto);

    /**
     * 用户 账号验证码登录
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/member" + "/loginByPhone")
    Result loginMemberByPhone(@RequestBody MemberDto memberDto);

    /**
     * 用户 账号密码登录
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/member" + "/loginMember")
    Result loginMember(@RequestBody MemberDto memberDto);

    /**
     * 验证码
     * 工人登录登陆
     *
     * @param phone
     * @return
     */
    @GetMapping("/worker" + "/wl/{phone}")
    Result loginWorkerByPhone(
            @PathVariable(value = "phone", required = true) String phone);

    /**
     * 手机号和密码
     * 工人登录登陆
     *
     * @param phone
     * @param password
     * @return
     */
    @GetMapping("/worker" + "/wl/{phone}/{password}")
    Result workerByPhoneOrPwd(
            @PathVariable(value = "phone", required = true) String phone,
            @PathVariable(value = "password", required = true) String password);
}

