package com.fantacg.user.controller;

import com.fantacg.common.pojo.user.Member;
import com.fantacg.common.pojo.user.Worker;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.dto.user.WorkerDto;
import com.fantacg.user.aspect.Requirespermissions;
import com.fantacg.user.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WorkerController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    WorkerService workerService;

    /**
     * 工人注册
     *
     * @param worker
     * @return
     */
    @PostMapping
    public Result workerRegister(@RequestBody Worker worker) {
        return this.workerService.workerRegister(worker);
    }


    /**
     * 手机号查询工人信息
     *
     * @param map {"phone":""}
     * @return
     */
    @PostMapping("/wphone")
    public Result workerByPhone(@RequestBody Map map) {
        return this.workerService.workerByPhone(map);
    }

    /**
     * 手机号和验证码 （工人登录登陆）
     * 远程调用使用
     *
     * @param phone
     * @return
     */
    @GetMapping("/wl/{phone}")
    public Result loginWorkerByPhone(@PathVariable(value = "phone", required = true) String phone) {
        return this.workerService.loginWorkerByPhone(phone);
    }

    /**
     * 手机号和密码 （工人登录登陆）
     * 远程调用使用
     *
     * @param phone
     * @param password
     * @return
     */
    @GetMapping("/wl/{phone}/{password}")
    public Result workerByPhoneOrPwd(@PathVariable(value = "phone", required = true) String phone, @PathVariable(value = "password", required = true) String password) {
        return this.workerService.workerByPhoneOrPwd(phone, password);
    }

    /**
     * 上传实人资料
     *
     * @param workerDto
     * @return
     */
    @PostMapping("/real_w")
    public Result addWorker(@RequestBody @Validated(QpGroup.Add.class) WorkerDto workerDto, BindingResult results) {
        if (results.hasErrors()) {
            results.getFieldError().getDefaultMessage();
            return Result.failure(results.getFieldError().getDefaultMessage());
        }
        return this.workerService.addWorker(workerDto);
    }

    /**
     * 初始化密码
     *
     * @param worker 工人信息
     * @return 初始化成功/失败
     */
    @PostMapping("/initializationPwd")
//    @Requirespermissions("user:initializationPwd")
    public Result initializationPwd(@RequestBody Worker worker) {
        return this.workerService.initializationPwd(worker);
    }



}
