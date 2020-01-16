package com.fantacg.answer.controller;


import com.fantacg.common.dto.answer.AddProjectTrainingDto;
import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.dto.answer.PtDto;
import com.fantacg.answer.service.ProjectTrainingService;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerController 项目培训基本信息类
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/pt")
public class ProjectTrainingController {

    @Autowired
    ProjectTrainingService projectTrainingService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 获取项目培训编号
     */
    @PostMapping("/sysNo")
    public Result getSysNo() {
        return this.projectTrainingService.getSysNo();
    }

    /**
     * 创建项目培训
     *
     * @param addProjectTrainingDto
     * @return
     */
    @PostMapping
    public Result queryProjectTrainingByPage(@RequestBody AddProjectTrainingDto addProjectTrainingDto) {
        return this.projectTrainingService.addProjectTraining(addProjectTrainingDto);
    }

    /**
     * 分页查询项目培训基本信息
     * @param page 分页
     * @param rows 分页
     * @param sortBy 排序
     * @param desc 排序
     * @param searchProjectCode 培训项目编码搜索
     * @param searchTrainingDate 培训日期搜索
     * @param searchTrainingName 课程名称搜索
     * @param trainingStatus 培训状态
     * @param searchTypeCode 培训类型搜索
     * @return
     */
    @GetMapping
    public Result queryProjectTrainingByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "searchProjectCode", required = false) String searchProjectCode,
            @RequestParam(value = "searchTrainingDate", required = false) Date searchTrainingDate,
            @RequestParam(value = "searchTrainingName", required = false) String searchTrainingName,
            @RequestParam(value = "trainingStatus", required = false) Integer trainingStatus,
            @RequestParam(value = "searchTypeCode", required = false) String searchTypeCode) {
        return this.projectTrainingService.queryProjectTrainingByPage(page, rows, sortBy, desc, searchProjectCode, searchTrainingDate, searchTrainingName, trainingStatus, searchTypeCode);
    }

    /**
     * （历史记录）分页查询项目培训记录
     *
     * @return
     */
    @PostMapping("/cardnum")
    public Result queryWorkerTrainingDetailByCardNum(@RequestBody PtDto dto) {
        return this.projectTrainingService.queryWorkerTrainingDetailByCardNum(dto);
    }

    /**
     * 单个删除项目培训
     *
     * @param dto
     * @return
     */
    @DeleteMapping
    public Result deleteProjectTraining(@RequestBody @Validated(QpGroup.Add.class) ProjectTrainingDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.projectTrainingService.deleteProjectTraining(dto);
    }


    /**
     * 结束项目培训考试
     *
     * @param dto
     * @return
     */
    @PutMapping
    public Result stopProjectTraining(@RequestBody @Validated(QpGroup.Add.class) ProjectTrainingDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.projectTrainingService.stopProjectTraining(dto);
    }


    /**
     * 查看项目培训考试详情
     *
     * @param dto
     * @return
     */
    @PostMapping("/det")
    public Result detailProjectTraining(@RequestBody @Validated(QpGroup.Add.class) ProjectTrainingDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.projectTrainingService.detailProjectTraining(dto);
    }

    /**
     * 提前开始项目培训培训
     *
     * @param dto
     * @return
     */
    @PostMapping("/start")
    public Result startProjectTraining(@RequestBody @Validated(QpGroup.Add.class) ProjectTrainingDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.projectTrainingService.startProjectTraining(dto);
    }


    /**
     * 答题详情（已答题）
     *
     * @param dto
     * @return
     */
    @PostMapping("/answerdet")
    public Result queryProjectTrainingAnswerDetail(@RequestBody @Validated(QpGroup.Add.class) PtDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.projectTrainingService.queryProjectTrainingAnswerDetail(dto);
    }


}
