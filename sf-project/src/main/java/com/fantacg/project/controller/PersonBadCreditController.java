package com.fantacg.project.controller;

import com.fantacg.common.pojo.project.PersonBadCreditInfo;
import com.fantacg.common.utils.Result;
import com.fantacg.project.service.PersonBadCreditInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname PersonBadCreditController 不良行为记录
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/badCredit")
public class PersonBadCreditController {

    @Autowired
    PersonBadCreditInfoService personBadCreditInfoService;


    /**
     * 分页查询不良行为记录
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param searchCateId
     * @return
     */
    @GetMapping("/page")
    public Result queryPersonBadCreditByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                             @RequestParam(value = "sortBy", required = false) String sortBy,
                                             @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                             @RequestParam(value = "key", required = false) String key,
                                             @RequestParam(value = "searchCateId", required = false) String searchCateId) {
        return this.personBadCreditInfoService.queryPersonBadCreditByPage(page, rows, sortBy, desc, key, searchCateId);
    }

    /**
     * 添加 不良行为记录
     *
     * @param personBadCreditInfo
     * @return
     */
    @PostMapping
    public Result addPersonBadCreditInfo(@RequestBody PersonBadCreditInfo personBadCreditInfo) {
        return this.personBadCreditInfoService.addPersonBadCreditInfo(personBadCreditInfo);
    }

}
