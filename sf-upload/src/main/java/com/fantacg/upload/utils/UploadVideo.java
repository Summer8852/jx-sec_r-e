package com.fantacg.upload.utils;

import com.aliyun.vod.upload.impl.PutObjectProgressListener;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoListRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoListResponse;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 以下Java示例代码演示了如何在服务端上传媒资文件至视频点播，媒资类型支持音频、视频和图片。
 * <p>
 * 一、音视频上传目前支持4种方式上传：
 * 1.上传本地文件，使用分片上传，并支持断点续传，参见testUploadVideo函数。
 * 1.1 当断点续传关闭时，最大支持上传任务执行时间为3000秒，具体可上传文件大小与您的网络带宽及磁盘读写能力有关。
 * 1.2 当断点续传开启时，最大支持48.8TB的单个文件，注意，断点续传开启后，上传任务执行过程中，同时会将当前上传位置写入本地磁盘文件，影响您上传文件的速度，请您根据文件大小选择是否开启
 * 2.上传网络流，可指定文件URL进行上传，不支持断点续传，最大支持5GB的单个文件。参见testUploadURLStream函数。
 * 3.上传文件流，可指定本地文件进行上传，不支持断点续传，最大支持5GB的单个文件。参见testUploadFileStream函数。
 * 4.流式上传，可指定输入流进行上传，支持文件流和网络流等，不支持断点续传，最大支持5GB的单个文件。参见testUploadStream函数。
 * <p>
 * 二、图片上传目前支持2种方式上传：
 * 1.上传本地文件，不支持断点续传，最大支持5GB的单个文件，参见testUploadImageLocalFile函数
 * 2.上传文件流和网络流，InputStream参数必选，不支持断点续传，最大支持5GB的单个文件。参见testUploadImageStream函数。
 * 注：图片上传完成后，会返回图片ID和图片地址，也可通过GetImageInfo查询图片信息，参见接口文档 https://help.aliyun.com/document_detail/89742.html
 * <p>
 * 三、上传进度回调通知：
 * 1.默认上传进度回调函数：视频点播上传SDK内部默认开启上传进度回调函数，输出不同事件通知的日志，您可以设置关闭该上传进度通知及日志输出；
 * 2.自定义上传进度回调函数：您可根据自已的业务场景重新定义不同事件处理的方式，只需要修改上传回调示例函数即可。
 * <p>
 * 注意：请替换示例中的必选参数，示例中的可选参数如果您不需要设置，请将其删除，以免设置无效参数值与您的预期不符。
 */

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname UploadVideo
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
public class UploadVideo {


    /**
     * 流式上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param fileName
     * @param inputStream
     */
    public static String testUploadStream(String accessKeyId, String accessKeySecret, VideoDto videoDto, String fileName, InputStream inputStream) {

        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, videoDto.getTitle(), fileName, inputStream);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        /*request.setShowWaterMark(true);*/

        /* 视频分类ID(可选) */
        if (StringUtils.isNotBlank(videoDto.getCateId())) {
            request.setCateId(Long.valueOf(videoDto.getCateId()));
        }

        /* 视频标签,多个用逗号分隔(可选) */
        if (StringUtils.isNotBlank(videoDto.getTags())) {
            request.setTags(videoDto.getTags());
        }

        /* 视频描述(可选) */
        if (StringUtils.isNotBlank(videoDto.getDescription())) {
            request.setDescription(videoDto.getDescription());
        }

        /* 封面图片(可选) */
        if (StringUtils.isNotBlank(videoDto.getCoverUrl())) {
            request.setCoverURL(videoDto.getCoverUrl());
        }
        /* 模板组ID(可选) */
//        request.setTemplateGroupId(videoDto.getTemplateGroupId());
        /* 存储区域(可选) */
        request.setStorageLocation(VideoConstant.storageLocation);
        /* 开启默认上传进度回调 */
        request.setPrintProgress(true);
        /* 设置自定义上传进度回调 (必须继承 ProgressListener) */
        request.setProgressListener(new PutObjectProgressListener());

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        //请求视频点播服务的请求ID
        log.info("请求视频点播服务的请求ID RequestId=" + response.getRequestId());
        if (response.isSuccess()) {

            try {
                DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
                VideoEncryption.submitTranscodeJobs(client, response.getVideoId(), videoDto.getTemplateGroupId());
            } catch (Exception e) {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
                throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
            }

            log.info("VideoId=" + response.getVideoId());
        } else {
            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            log.info("VideoId=" + response.getVideoId());
            log.info("ErrorCode=" + response.getCode());
            log.info("ErrorMessage=" + response.getMessage());
        }
        return response.getVideoId();
    }

    /**
     * 获取视频信息
     *
     * @param videoId 发送请求客户端
     * @return GetVideoInfoResponse 获取视频信息响应数据
     * @throws Exception
     */
    public static GetVideoInfoResponse getVideoInfo(String videoId) throws Exception {
        log.info("accessKeyId:" + VideoConstant.accessKeyId);
        DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }

    private InputStream getFileStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    private InputStream getUrlStream(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

}
