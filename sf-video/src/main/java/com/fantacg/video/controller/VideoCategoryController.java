package com.fantacg.video.controller;


import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.video.VideoCategory;
import com.fantacg.video.aspect.Requirespermissions;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.common.pojo.video.VideoCategory;
import com.fantacg.video.service.VideoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoCategoryController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/category")
public class VideoCategoryController {


    @Autowired
    private VideoCategoryService videoCategoryService;

    /**
     * 添加分类 （官方管理员才能添加）
     *
     * @param videoCategory 分类信息
     * @return 添加成功/失败
     */
    @PostMapping("/save")
    @Requirespermissions("video:category:save")
    public Result save(@RequestBody VideoCategory videoCategory) {
        if (StringUtils.isEmpty(videoCategory.getName())) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.videoCategoryService.saveVideoCategory(videoCategory);
    }


    /**
     * 查询类目
     *
     * @param pid 分类父级id
     * @return 类目列表
     */
    @GetMapping("/list")
    public Result queryCategoryListByParentId(@RequestParam(value = "pid", defaultValue = "-1L") Long pid) {
        try {
            // 执行查询操作
            return this.videoCategoryService.queryCategoryListByParentId(pid);
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 根据id删除分类（官方管理员权限）
     *
     * @param id 分类id
     * @return 删除 成功/失败
     */
    @DeleteMapping("delete/{id}")
    @Requirespermissions("video:category:del")
    public Result deleteById(@PathVariable("id") Long id) {
        return this.videoCategoryService.deleteById(id);
    }


    /**
     * 修改分类
     *
     * @param videoCategory 分类信息
     * @return 修改成功/失败
     */
    @PutMapping("/update")
    @Requirespermissions("video:category:update")
    public Result update(@RequestBody VideoCategory videoCategory) {
        if (StringUtils.isEmpty(videoCategory.getId()) && StringUtils.isEmpty(videoCategory.getName())) {
            return Result.success(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.videoCategoryService.update(videoCategory);

    }

}
