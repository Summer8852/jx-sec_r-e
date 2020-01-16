package com.fantacg.upload.controller;

import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentMap;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname UploadController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@RestController
public class UploadController {

    @Autowired
    UploadService uploadService;

    /**
     * 视频上传阿里云
     *
     * @param videoDto
     * @param result
     * @param request
     * @param videoFile
     * @return
     */
    @PostMapping("/upload/aliyun")
    public Result uploadAliyun(@Validated(QpGroup.Add.class) VideoDto videoDto, BindingResult result, HttpServletRequest request,
                               @RequestParam(value = "file") MultipartFile videoFile) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }

        return this.uploadService.uploadAliyun(videoDto, videoFile, request);
    }


    /**
     * 视频上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadVideo")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        return uploadService.uploadVideo(file);
    }


    /**
     * 删除文件
     *
     * @param
     * @return
     */
    @PostMapping("/uploadDel")
    public Result uploadDelImage(@RequestBody ConcurrentMap concurrentMap) {
        return this.uploadService.uploadDelImage(concurrentMap);
    }


    /**
     * 识别身份证信息
     *
     * @param file
     * @param fileType 身份证含照片的一面请输入【front】；身份证带国徽的一面请输入【reverse】
     * @return
     */
    @PostMapping("/uploadIdCard/{fileType}")
    public Result uploadIdCard(
            @RequestParam("file") MultipartFile file,
            @PathVariable("fileType") String fileType) {
        return this.uploadService.uploadIdCard(file, fileType);
    }


    /**
     * 图片的上传 实人对比
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/{fileType}")
    public Result uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("url") String url,
            @PathVariable("fileType") String fileType) {
        return uploadService.uploadImages(file, url, fileType);
    }

    /**
     * 上传身份阅读器的图片/上传附件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/cardimg")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("cardNum") String cardNum) {
        return this.uploadService.uploadFile(file, cardNum);
    }

    /**
     * 视频转码
     */
    @GetMapping("/upload/encryption/{videoId}")
    public Result videoEncryption(
            @PathVariable("videoId") String videoId) {
        return uploadService.videoEncryption(videoId);
    }


    /**
     * 上传模板文档
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/word")
    public Result uploadWord(@RequestParam("file") MultipartFile file) {
        return this.uploadService.uploadWord(file);
    }

}
