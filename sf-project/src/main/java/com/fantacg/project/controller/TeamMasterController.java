package com.fantacg.project.controller;


import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.Result;
import com.fantacg.common.pojo.project.TeamMaster;
import com.fantacg.project.service.TeamMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname TeamMasterController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/teammaster")
public class TeamMasterController {


    @Autowired
    TeamMasterService teamMasterService;

    /**
     * 添加班组
     * @param teamMasters 班组对象
     * @return 返回是否添加
     */
    @PostMapping
    public Result installTeamMaster(@RequestBody TeamMaster teamMasters){
        return teamMasterService.installTeamMaster(teamMasters);
    }

    /**
     * 分页查询班组列表
     * @param page 分页
     * @param rows 分页
     * @param sortBy
     * @param desc
     * @param key
     * @param projectCode
     * @return
     */
    @GetMapping("/page")
    public Result queryProjectByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "projectCode", defaultValue = "") String projectCode) {
        PageResult<TeamMaster> result = this.teamMasterService.selectTeamMasterByPage(page,rows,sortBy,desc, key,projectCode);
        return Result.success(result);
    }

    /**
     * Id查询班组详情
     * @param id 班组id
     * @return
     */
    @GetMapping("/{id}")
    public Result teamMasterDetail(@PathVariable("id")String id){
       return this.teamMasterService.selectTeamMasterDetail(id);
    }

    /**
     * 修改班组
     * @param teamMasters 班组对象
     * @return 是否修改成功
     */
    @PutMapping
    public Result updateTeamMaster(@RequestBody TeamMaster teamMasters){
        return this.teamMasterService.updateTeamMaster(teamMasters);
    }

    /**
     * 删除班组
     * @param id 班组id
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public Result updateTeamMaster(@PathVariable("id")Long id){
        return this.teamMasterService.updateTeamMasterIsDel(id);
    }


}
