package com.fantacg.answer.controller;


import com.fantacg.common.dto.answer.AddQuickProjectTrainingDto;
import com.fantacg.answer.service.QuickProjectTrainingService;
import com.fantacg.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目培训基本信息类
 * @author DUPENGFEI
 */
@RestController
@RequestMapping("/quickpt")
public class QuickProjectTrainingController {

    @Autowired
    QuickProjectTrainingService quickProjectTrainingService;

    /**
     * 创建快速学习
     * @param addQuickProjectTrainingDto
     * @return
     */
    @PostMapping
    public Result addQuickProjectTraining(@RequestBody AddQuickProjectTrainingDto addQuickProjectTrainingDto){
        return this.quickProjectTrainingService.addQuickProjectTraining(addQuickProjectTrainingDto);
    }

    /**
     * 根据状态查询快速学习列表
     * @param status -1删除 0未开始 1进行中 2结束
     * @return
     */
    @GetMapping("/{status}")
    public Result queryQuickProjectTraining(@PathVariable("status") Integer status) {
        return this.quickProjectTrainingService.queryQuickProjectTraining(status);
    }

}
