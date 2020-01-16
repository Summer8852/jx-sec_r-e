package com.fantacg.project.controller;

import com.fantacg.common.pojo.worker.WorkerInfo;
import com.fantacg.common.utils.Result;
import com.fantacg.common.dto.project.ProjectWorkerDTO;
import com.fantacg.common.pojo.project.ProjectWorker;
import com.fantacg.project.service.ProjectWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectWorkerController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/pw")
public class ProjectWorkerController {

    @Autowired
    ProjectWorkerService projectWorkerService;

    /**
     * 添加项目参建单位-班组-人员数据
     * 同一项目 只能加入一个班组
     * @param workerInfo
     * @return
     */
    @PostMapping
    public Result addProjectWorker(@RequestBody WorkerInfo workerInfo) {
        return this.projectWorkerService.addProjectWorker(workerInfo);
    }

    /**
     * 筛选工人实名信息列表(可选分页/不分页)
     * @return
     */
    @PostMapping("/screeningProjectWorker")
    public Result screeningProjectWorker(@RequestBody ProjectWorkerDTO dto) {
        return this.projectWorkerService.screeningProjectWorker(dto);
    }

    /**
     * 筛选工人实名信息列表(创建培训时使用)
     * @return
     */
    @PostMapping("/screeningPtm")
    public Result screeningProjectWorkerByTraining(@RequestBody ProjectWorkerDTO dto) {
        return this.projectWorkerService.screeningProjectWorkerByTraining(dto);
    }

    /**
     * 批量或单个删除班组人员信息
     * @param lists
     * @return
     */
    @DeleteMapping
    public Result delProjectWorker(@RequestBody List<Long> lists){
        return this.projectWorkerService.delProjectWorker(lists);
    }


    /**
     * 分页查询班组人员信息列表
     * @param map {"page":1,"rows":10,"teamSysNo":"班组ID"}
     * @return
     */
    @PostMapping("/teamsysno")
    public Result queryProjectWorkerByTeamSysNo(@RequestBody Map<String,Object> map) {
        return this.projectWorkerService.queryProjectWorkerBySysNoPage(map);
    }


    /**
     * 是否成为班组 组长
     * @param projectWorker 需要成为班组组长的人员信息
     *
     * @return 返回是否成功
     */
    @PostMapping("/isBecomeTeamLeader")
    public Result isBecomeTeamLeader(@RequestBody ProjectWorker projectWorker){
        return this.projectWorkerService.isBecomeTeamLeader(projectWorker);
    }

}
