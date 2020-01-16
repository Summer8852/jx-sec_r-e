package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.project.service.NationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname NationController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/nation")
public class NationController {
    @Autowired
    NationService nationService;


    /**
     * 查询世界各国和地区名称代码
     *
     * @return
     */
    @GetMapping
    public Result queryNation() {
        return this.nationService.queryNation();
    }


    /**
     * Id查询世界各国和地区名称代码
     *
     * @return id
     */
    @GetMapping("/{id}")
    public Result queryNation(@PathVariable("id") Long id) {
        return this.nationService.queryNationById(id);
    }

}
