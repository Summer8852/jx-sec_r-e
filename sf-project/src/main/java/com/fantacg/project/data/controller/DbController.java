package com.fantacg.project.data.controller;

import com.fantacg.common.enums.Check;
import com.fantacg.common.utils.Result;

import com.fantacg.project.aspect.Requirespermissions;
import com.fantacg.project.service.data.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname DbController 数据库
 * @Created by Dupengfei 2020-01-06 11:08
 * @Version 2.0
 */
@RestController
@RequestMapping("/db")
public class DbController {

    @Autowired
    DbService dbService;

    /**
     * 分页查询
     * 获取所有表结构(TABLES)
     *
     * @return
     */
    @GetMapping("/tablesALL")
    @Requirespermissions("db:tablesALL")
    public Result tablesALL(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "tableSchema", defaultValue = "fantacg") String tableSchema) {
        System.out.println("tablesALL");
        return dbService.tablesALL(tableSchema, page, rows);
    }


    /**
     * 获取所有表结构(TABLES)
     *
     * @return
     */
    @GetMapping("/columns/{tableName}")
    @Requirespermissions("db:columns")
    public Result columns(
            @RequestParam(value = "tableSchema", defaultValue = "fantacg") String tableSchema,
            @PathVariable("tableName") String tableName) {
        return dbService.columns(tableSchema, tableName);
    }


}
