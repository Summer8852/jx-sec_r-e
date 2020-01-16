package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.common.pojo.project.PersonGoodCreditInfo;
import com.fantacg.project.service.PersonGoodCreditInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PersonGoodCreditContooller 良好行为记录
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/goodCredit")
public class PersonGoodCreditContooller {


    @Autowired
    PersonGoodCreditInfoService personGoodCreditInfoService;

    /**
     * 分页查询行为良好记录
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param searchCateId
     * @return
     */
    @GetMapping("/page")
    public Result queryProjectByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                     @RequestParam(value = "sortBy", required = false) String sortBy,
                                     @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                     @RequestParam(value = "key", required = false) String key,
                                     @RequestParam(value = "searchCateId", required = false) String searchCateId) {
        return this.personGoodCreditInfoService.queryProjectByPage(page, rows, sortBy, desc, key, searchCateId);
    }

    /**
     * 添加 行为良好记录
     * @param personGoodCreditInfo
     * @return
     */
    @PostMapping
    public Result addPersonGoodCreditInfo(@RequestBody PersonGoodCreditInfo personGoodCreditInfo){
        return this.personGoodCreditInfoService.addPersonGoodCreditInfo(personGoodCreditInfo);
    }



}
