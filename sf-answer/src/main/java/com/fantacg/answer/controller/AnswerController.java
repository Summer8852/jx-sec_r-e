package com.fantacg.answer.controller;


import com.fantacg.answer.service.AnswerService;
import com.fantacg.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
public class AnswerController {

    @Autowired
    AnswerService answerService;

    /**
     * 根据工种查询考试题目
     *
     * @param workType
     * @return
     */
    @GetMapping("/{workType}")
    public Result answerByWorkType(@PathVariable("workType") String workType) {
        return this.answerService.queryAnswerListByWorkType(workType);
    }

    /**
     * 根据视频id 查询题目
     */
    @PostMapping("/vid")
    public Result answerVideoById(@RequestBody List<String> list) {
        return this.answerService.queryAnswerVideoById(list);
    }



}
