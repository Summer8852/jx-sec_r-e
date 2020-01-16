package com.fantacg.project.data.controller;

import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.utils.Result;
import com.fantacg.project.service.data.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname DataController 数据上报api 数据接收
 * @Created by Dupengfei 2019-12-20 14:52
 * @Version 2.0
 */
@RestController
@RequestMapping("/receive")
public class ReceiveController {

    @Autowired
    ReceiveService receiveService;

    @PostMapping
    public Result receiveSafetyEducation(@RequestBody SafetyEducation safetyEducation, HttpServletRequest request) {
        return this.receiveService.receiveSafetyEducation(safetyEducation,request);
    }

}
