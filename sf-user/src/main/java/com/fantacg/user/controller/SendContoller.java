package com.fantacg.user.controller;

import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname SendContoller 发送短信或邮箱
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("send")
public class SendContoller {

    @Autowired
    SendService sendService;


    /**
     * 注册发送手机验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/register/code/{phone}")
    public Result sendVerifyCode(@PathVariable("phone") String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.sendService.sendVerifyCode(phone);
    }

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email")
    public Result sendVerifyCode(@RequestBody MemberDto memberDto) {
        return this.sendService.sendMail(memberDto);
    }

    /**
     * 登录发送手机验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/login/code/{phone}")
    public Result sendLoginCode(@PathVariable("phone") String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.sendService.sendLoginCode(phone);
    }


    /**
     * 忘记密码获取验证码
     */
    @GetMapping("/forgetCode/{phone}")
    public Result forgetCode(@PathVariable("phone") String phone) {
        return sendService.forgetCode(phone);
    }

    /**
     * 发送绑定验证码
     *
     * @param type
     * @param param
     * @return
     */
    @GetMapping("/sendBindCode/{type}/{param}")
    public Result sendBindCode(@PathVariable("type") String type, @PathVariable("param") String param) {
        return sendService.sendBindCode(type, param);
    }

    /**
     * 工人注册发送手机验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/wrc/{phone}")
    public Result sendWorkerVerifyCode(@PathVariable("phone") String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.sendService.sendWorkerVerifyCode(AesEncrypt.decryptAES(phone));
    }

    /**
     * 工人登录发送手机验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/wlc/{phone}")
    public Result sendWorkerLoginCode(@PathVariable("phone") String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.sendService.sendWorkerLoginCode(phone);
    }

}
