package com.fantacg.answer.controller;

import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.pojo.answer.ProjectTrainingDetail;
import com.fantacg.answer.service.ProjectTrainingDetailService;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname  ProjectTrainingDetailController 项目培训详情类
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/projTrainDetail")
public class ProjectTrainingDetailController {
    @Autowired
    ProjectTrainingDetailService detailService;


    /**
     * 分页查询培训人在当前账号下的培训详情
     *
     * @param page   分页
     * @param rows   分页
     * @param detail 项目培训基本信息 （AES加密身份证号）
     * @return
     */
    @PostMapping("/detailsByCardId")
    public Result queryDetailsByCardId(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestBody ProjectTrainingDetail detail) {
        return this.detailService.queryDetailsByCardId(page, rows, detail);
    }


    /**
     * 查询项目培训详情列表（答题列表）
     *
     * @param dto 项目培训基本信息 （培训id）
     * @return 详情列表
     */
    @PostMapping("/detailsByPtid")
    public Result queryDetailByProjectTrainingId(@RequestBody @Validated(QpGroup.Add.class) ProjectTrainingDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.detailService.queryDetailsByProjectTrainingId(dto);
    }


}
