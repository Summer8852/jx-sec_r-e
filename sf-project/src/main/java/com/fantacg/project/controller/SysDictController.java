package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.project.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname SysDictController 字典表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/dict")
public class SysDictController {
    @Autowired
    SysDictService sysDictService;

    /**
     * 分页查询
     *
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/dictpage")
    public Result selectDictPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "key", required = false) String key) {
        return this.sysDictService.selectDictPage(page, rows, key);
    }

    /**
     * 查询字典表Key
     *
     * @return
     */
    @GetMapping("/distkeylist")
    public Result selectDistKeyList() {
        return this.sysDictService.selectDictKeyList();
    }

    /**
     * 根据type 查询
     *
     * @param type
     * @return
     */
    @GetMapping("/{type}")
    public Result getSelectParams(@PathVariable("type") String type) {
        return this.sysDictService.selectDictByType(type);
    }

    /**
     * 根据id查询字典表
     *
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public Result selectDictById(@PathVariable("id") String id) {
        return this.sysDictService.selectDictById(id);
    }

}
