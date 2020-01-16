package com.fantacg.project.controller;

import com.fantacg.common.dto.project.ProjectInfos;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.pojo.project.ProjectInfo;
import com.fantacg.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectInfoController 项目管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    @Autowired
    ProjectInfoService projectInfoService;

    /**
     * 分页查询项目管理
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param searchCateId
     * @return
     */
    @GetMapping("/page")
    public Result queryProjectByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "searchCateId", required = false) String searchCateId) {
        PageResult<ProjectInfo> result = this.projectInfoService.queryProjectByPage(page,rows,sortBy,desc, key,searchCateId);
        return Result.success(result);
    }

    /**
     * 新增项目
     * @param projectInfos
     * @return
     */
    @PostMapping
    public Result saveProjectInfo(@RequestBody @Validated(QpGroup.Add.class) ProjectInfos projectInfos, BindingResult result){
        if (result.hasErrors()) {
           return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return  projectInfoService.saveProject(projectInfos);
    }

    /**
     * 修改项目
     * @param projectInfos
     * @return
     * @Requirespermissions("project")
     */
    @PutMapping
    public Result editProject(@RequestBody @Validated(QpGroup.Update.class) ProjectInfos projectInfos, BindingResult result){
        if (result.hasErrors()) {
           return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.projectInfoService.editProjectInfo(projectInfos);
    }


    /**
     * 查询详情
     */
    @GetMapping("/detail/{id}")
    public Result selectProjectDetail(@PathVariable("id")Long id){
        return  projectInfoService.selectProjectDetail(id);
    }

    /**
     *  删除项目
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delProjectInfo(@PathVariable("id")Long id){
         return  this.projectInfoService.delProjectInfo(id);
    }

    /**
     * 查询项目id  编号 和 项目名称
     */
    @GetMapping("/pcode")
    public Result getProjectCode(){
        return  this.projectInfoService.getProjectCode();
    }




    /**
     * @param code 根据平台第三方项目编号 生成项目二维码
     * @return
     */
    @GetMapping("/generatorQrCode/{code}")
    public Result generatorQrCode(@PathVariable("code") String code){
            projectInfoService.generatorQrCode(code);
            return Result.success();
    }


    /**
     * 删除项目施工许可证数
     */
    @DeleteMapping("removePbl/{pId}/{pblId}")
    public Result deletePBL(@PathVariable("pId")String pId,@PathVariable("pblId")Long pblId){
        return projectInfoService.deletePBL(pId,pblId);
    }

    /**
     * 删除项目参建单位信息数
     */
    @DeleteMapping("removePc/{pId}/{pcId}")
    public Result deletePC(@PathVariable("pId")String pId,@PathVariable("pcId")Long pcId){
        return projectInfoService.deletePC(pId,pcId);
    }

    /**
     * 删除项目参建单位信息数(接触项目与班组关系 并未删除班组)
     */
    @DeleteMapping("removeTM/{pId}/{tmId}")
    public Result deleteTM(@PathVariable("pId")String pId,@PathVariable("tmId")String tmId){
        return projectInfoService.deleteTM(pId,tmId);
    }

}
