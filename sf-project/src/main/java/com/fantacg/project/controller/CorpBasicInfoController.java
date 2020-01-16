package com.fantacg.project.controller;


import com.fantacg.common.dto.project.CorpBasicInfos;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fantacg.project.service.CorpBasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname CorpBasicInfoController  企业基本信息
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/corpBasicInfo")
public class CorpBasicInfoController {


    @Autowired
    CorpBasicInfoService corpBasicInfoService;


    /**
     * 添加企业基本信息
     */
    @PostMapping
    public Result addCorpBasicInfo(@RequestBody CorpBasicInfos corpbasicinfos) {
        return this.corpBasicInfoService.addCorpBasicInfo(corpbasicinfos);
    }


    /**
     * 分页查询查询企业基本信息
     */
    @GetMapping
    public Result queryCorpBasicInfo(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", defaultValue = "") String key,
            @RequestParam(value = "corpType", required = false) String corpType,
            @RequestParam(value = "searchCateId", required = false) String searchCateId) {
        return this.corpBasicInfoService.queryCorpBasicInfoList(page, rows, sortBy, desc, key, corpType, searchCateId);
    }

    /**
     * 删除企业
     */
    @DeleteMapping("/{id}")
    public Result removeCorpBasicInfo(@PathVariable("id") Long id) {
        return this.corpBasicInfoService.removeCorpBasicInfo(id);
    }

    /**
     * 查询企业详情
     */
    @GetMapping("/{id}")
    public Result selectCorpBasicInfoDetail(@PathVariable("id") Long id) {
        try {
            return this.corpBasicInfoService.selectCorpBasicInfoDetail(id);
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 修改企业
     */
    @PutMapping("/udtCBI")
    public Result updateMCorpBasicInfo(@RequestBody CorpBasicInfos corpBasicInfos) {
        return this.corpBasicInfoService.updateBasicInfo(corpBasicInfos);
    }


    /**
     * 搜索自己的企业
     */
    @GetMapping("/search")
    public Result search() {
        return this.corpBasicInfoService.searchCorpBasicInfo(null);
    }
}
