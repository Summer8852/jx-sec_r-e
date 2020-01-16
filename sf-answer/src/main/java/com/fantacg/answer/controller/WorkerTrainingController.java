package com.fantacg.answer.controller;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.dto.answer.PtDto;
import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.pojo.answer.AnswerLog;
import com.fantacg.answer.service.AnswerLogService;
import com.fantacg.answer.service.ProjectTrainingService;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.vo.answer.ReceiveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname WorkerTrainingController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/wtraining")
public class WorkerTrainingController {

    @Autowired
    ProjectTrainingService projectTrainingService;
    @Autowired
    AnswerLogService answerLogService;


    /**
     * 分页查询工人查询项目培训列表
     *
     * @param map
     * @return
     */
    @PostMapping
    public Result queryWorkerTrainings(@RequestBody Map<String, Object> map) {
        return projectTrainingService.queryWorkerTrainings(map);
    }

    /**
     * 查询工人项目培训观看视频
     *
     * @param dto
     * @return
     */
    @PostMapping("/wtv")
    public Result queryWorkerTrainingDetails(@RequestBody @Validated(QpGroup.Add.class) PtDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return projectTrainingService.queryWorkerTrainingVideos(dto);
    }

    /**
     * 查询工人项目培训的试题
     *
     * @param dto
     * @return
     */
    @PostMapping("/wta")
    public Result queryWorkerTrainingAnswers(@RequestBody @Validated(QpGroup.Add.class)
                                                     PtDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return projectTrainingService.queryWorkerTrainingAnswers(dto);
    }

    /**
     * 工人扫码 查询 项目培训的试题
     *
     * @param dto
     * @return
     */
    @PostMapping("/wtaQR")
    public Result queryWorkerTrainingAnswersQR(@RequestBody @Validated(QpGroup.Add.class)
                                                       PtDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return projectTrainingService.queryWorkerTrainingAnswersQR(dto);
    }

    /**
     * 全部答完一起提交
     *
     * @param answerLog
     * @param result
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/wal")
    public Result answer1(@RequestBody @Validated(QpGroup.Add.class) AnswerLog answerLog, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.answerLogService.addAnswerLogs(answerLog);
    }

    @PostMapping("/safetyEducation")
    public Result safetyEducation(@RequestBody PlatformKey platformKey) {
        System.out.println(JSON.toJSONString(platformKey));
        return this.projectTrainingService.receiveSafetyEducation(platformKey);
    }

}
