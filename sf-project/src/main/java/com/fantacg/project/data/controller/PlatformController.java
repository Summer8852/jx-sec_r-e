package com.fantacg.project.data.controller;


import com.fantacg.common.pojo.project.Platform;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.project.aspect.Requirespermissions;
import com.fantacg.project.service.PlatformKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname EquipmentController 设备
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/equipment")
public class PlatformController {

    @Autowired
    PlatformKeyService platformKeyService;

    /**
     * 添加设备及平台
     * @return
     */
    @PostMapping("/addPlatform")
    @Requirespermissions("equipment:addPlatform")
    public Result addPlatform(@RequestBody @Validated(QpGroup.Add.class) Platform platform, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.platformKeyService.addPlatform(platform);
    }

    /**
     * 查询全部设备
     * @return
     */
    @GetMapping("pagePlatform")
    @Requirespermissions("equipment:pagePlatform")
    public Result queryPlatform(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return this.platformKeyService.pagePlatform(page,rows);
    }

    /**
     * 删除设备及平台
     * @return
     */
    @PostMapping("/delPlatform")
    @Requirespermissions("equipment:delPlatform")
    public Result delPlatform(@RequestBody @Validated(QpGroup.Del.class) Platform platform, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.platformKeyService.delPlatform(platform);
    }


    /**
     * 系统生成
     *
     * @param platformKey
     * @return
     */
    @PostMapping("/addPlatformKey")
    public Result addPlatformKey(@RequestBody @Validated(QpGroup.Add.class) PlatformKey platformKey, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.platformKeyService.addPlatformKey(platformKey);
    }

    /**
     * 查询所有设备
     *
     * @return
     */
    @GetMapping("/queryPlatformKey")
    public Result queryPlatformKey(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return this.platformKeyService.queryPlatformKey(page,rows);
    }


    /**
     * 远程调用使用
     * @param platformKey
     * @return
     */
    @PostMapping("/queryPlatformKeyByKey")
    public PlatformKey queryPlatformKeyByKey(@RequestBody PlatformKey platformKey){
        return this.platformKeyService.queryPlatformKeyByKey(platformKey);
    }


    /**
     * 删除
     *
     * @param platformKey
     * @return
     */
    @DeleteMapping("/delPlatformKeyByKey")
    public Result delPlatformKeyByKey(@RequestBody PlatformKey platformKey) {
        return this.platformKeyService.delPlatformKeyByKey(platformKey);
    }

    /**
     * 查询单个用户所有设备
     *
     * @return
     */
    @PostMapping("/queryPlatformKeyByAccount")
    public Result queryPlatformKeyByAccount(@RequestBody PlatformKey platformKey) {
        return this.platformKeyService.queryPlatformKeyByAccount(platformKey);
    }
}
