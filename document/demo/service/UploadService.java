package com.fantacg.upload.service;

import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentMap;

/**
 * @author DUPENGFEI
 * @date 2018/9/16
 */
public interface UploadService {

    /**
     * 阿里云校验(上传图片)
     * @param file
     * @param url
     * @param fileType
     * @return
     */
    Result uploadImages(MultipartFile file,String url ,String fileType);

    /**
     * 上传视频
     * @param file
     * @return
     */
    Result uploadVideo(MultipartFile file);

    /**
     * 上传身份证正反面图 识别后成功上传
     * @param file
     * @param fileType
     * @return
     */
    Result uploadIdCard(MultipartFile file, String fileType);

    /**
     * 删除未使用的图片
     * @param concurrentMap
     * @return
     */
    Result uploadDelImage(ConcurrentMap concurrentMap);

    /**
     * 视频上传阿里云
     * @param videoDto
     * @param videoFile
     * @param request
     * @return
     */
    Result uploadAliyun(VideoDto videoDto, MultipartFile videoFile, HttpServletRequest request);

    /**
     * 视频转码
     * @param videoId
     * @return
     */
    Result videoEncryption(String videoId);

    /**
     * 上传文件（上传附件）
     * @param file
     * @return
     */
    Result uploadFile(MultipartFile file);
    
}
