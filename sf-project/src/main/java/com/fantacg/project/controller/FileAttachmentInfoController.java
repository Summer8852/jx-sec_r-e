package com.fantacg.project.controller;

import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.pojo.project.FileAttachmentInfo;
import com.fantacg.project.service.FileAttachmentInfoService;
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
 * @Classname FileAttachmentInfoController  附件
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/file")
public class FileAttachmentInfoController {

    @Autowired
    FileAttachmentInfoService fileAttachmentInfoService;

    /**
     * 添加附件
     *
     * @param info
     * @param result
     * @return
     */
    @PostMapping
    public Result addFileAttachmentInfo(@RequestBody @Validated(QpGroup.Add.class) FileAttachmentInfo info, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.fileAttachmentInfoService.addFileAttachmentInfo(info);
    }

    /**
     * 删除附件
     */
    @DeleteMapping("/{id}")
    public Result deleteFileAttachmentInfo(@PathVariable("id") Long id) {
        return this.fileAttachmentInfoService.deleteFileAttachmentInfo(id);
    }
}
