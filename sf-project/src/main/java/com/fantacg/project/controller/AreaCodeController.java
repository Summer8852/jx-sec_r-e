package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.project.service.AreaCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname AreaCodeController 注册地区编码
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/ac")
public class AreaCodeController {

    @Autowired
    AreaCodeService areaCodeService;

    /**
     * 查询 注册地区编码 列表
     *
     * @return
     */
    @GetMapping
    public Result queryAreaCodeList() {
        return areaCodeService.queryAreaCodeList();
    }

    /**
     * code 查询 注册地区编码
     *
     * @param code
     * @return
     */
    @GetMapping("/{code}")
    public Result selectAreaCodeName(@PathVariable("code") String code) {
        return areaCodeService.selectAreaCodeName(code);
    }

}
