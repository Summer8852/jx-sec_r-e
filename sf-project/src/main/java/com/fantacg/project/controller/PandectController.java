package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.project.service.PandectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname PandectController 总览 (数据待定)
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/pandect")
public class PandectController {

    @Autowired
    PandectService pandectService;

    /**
     * 查询 项目 班组 培训数
     *
     * @return 项目 班组 培训数
     */
    @GetMapping("/protmpw")
    public Result queryProTmPwCount() {
        return this.pandectService.queryProTmPwCount();
    }

    /**
     * 查询最新 答题列表
     *
     * @param page
     * @return 答题列表
     */
    @GetMapping("pts/{page}")
    public Result query(@PathVariable("page") Integer page) {
        return this.pandectService.queryPtList(page);
    }

    /**
     * 查询 及格 不及格人数
     *
     * @return 及格 不及格人数
     */
    @GetMapping("/passrate")
    public Result queryPassRate() {
        return this.pandectService.queryPassRate();
    }


    /**
     * 查询一周内每天答题人数
     *
     * @return 一周内每天答题人数
     */
    @GetMapping("/weekAnswerNum")
    public Result queryWeekAnswerNum() {
        return this.pandectService.queryWeekAnswerNum();
    }


    /**
     * 查询每个工种占比
     *
     * @return 每个工种占比
     */
    @GetMapping("/workerProportion")
    public Result queryWorkerProportion() {
        return this.pandectService.queryWorkerProportion();
    }

}

