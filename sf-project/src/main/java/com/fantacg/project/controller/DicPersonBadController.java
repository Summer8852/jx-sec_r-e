package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.project.service.DicPersonBadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname DicPersonBadController 人员不良行为字典表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/dicPersonBad")
public class DicPersonBadController {

    @Autowired
    DicPersonBadService dicPersonBadService;

    @GetMapping
    public Result queryDicPersonBadAll() {
        return this.dicPersonBadService.queryDicPersonBadAll();
    }

}
