package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.project.service.DictionaryKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DictionaryKeyController  字典表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("dictionarykey")
public class DictionaryKeyController {

    @Autowired
    DictionaryKeyService dictionaryKeyService;

    /**
     * 查询字典表Key
     *
     * @return
     */
    @GetMapping
    public Result selectDictionaryKeyList() {
        return this.dictionaryKeyService.selectDictionaryKeyList();
    }


    /**
     * 根据type 查询
     *
     * @param type
     * @return
     */
    @GetMapping("/{type}")
    public Result getSelectParams(@PathVariable("type") String type) {
        return dictionaryKeyService.selectDictByType(type);
    }

    /**
     * 根据type,value查询
     *
     * @param type
     * @return
     */
    @GetMapping("/{type}/{value}")
    public Result getDictionaryByValue(@PathVariable("type") String type, @PathVariable("value") String value) {
        return dictionaryKeyService.getDictionaryByValue(type, value);
    }






    /**
     * 企业资质资格专业类别字典表 列表
     *
     * @param parentId
     * @return
     */
    @GetMapping("/tradetypebound/{parentId}")
    public Result getDicTradeTypeBoundByParentId(@PathVariable("parentId") String parentId) throws Exception {
        return dictionaryKeyService.getDicTradeTypeBoundByParentId(parentId);
    }


    /**
     * 根据value 查询 企业资质资格专业类别
     *
     * @param value
     * @return
     */
    @GetMapping("/tradetypebound/value/{value}")
    public Result getDicTradeTypeBoundByV(@PathVariable("value") String value) throws Exception {
        return dictionaryKeyService.getDicTradeTypeBoundByV(value);
    }







    /**
     * 企业资质等级 列表
     *
     * @param parentId
     * @return
     */
    @GetMapping("/certtitlelevel/{parentId}")
    public Result getCertTitleLevelByParentId(@PathVariable("parentId") String parentId) throws Exception {
        return dictionaryKeyService.getCertTitleLevelByParentId(parentId);
    }


    /**
     * 根据parentId value 查询 企业资质等级
     *
     * @param value
     * @return
     */
    @GetMapping("/certtitlelevel/value/{parentId}/{value}")
    public Result getCertTitleLevelByValue(@PathVariable("parentId") String parentId, @PathVariable("value") String value) throws Exception {
        return dictionaryKeyService.getCertTitleLevelByValue(parentId, value);
    }

}
