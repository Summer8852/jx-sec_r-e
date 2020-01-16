package com.fantacg.upload.utils;

import com.aliyun.vod.upload.impl.PutObjectProgressListener;
import com.aliyun.vod.upload.impl.UploadImageImpl;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.*;
import com.aliyun.vod.upload.resp.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.dto.video.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.util.Date;
import java.util.SimpleTimeZone;

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
 * @author DUPENGFEI
 */

@Slf4j
public class UploadVideo {

    /**
     * 本地文件上传接口
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @return
     */
    public String testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        request.setTemplateGroupId(VideoConstant.templateGroupId);
        /* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
        注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启*/
        request.setEnableCheckpoint(false);
        /* OSS慢请求日志打印超时时间，是指每个分片上传时间超过该阈值时会打印debug日志，如果想屏蔽此日志，请调整该阈值。单位: 毫秒，默认为300000毫秒*/
        //request.setSlowRequestsThreshold(300000L);
        /* 可指定每个分片慢请求时打印日志的时间阈值，默认为300s*/
        //request.setSlowRequestsThreshold(300000L);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setIsShowWaterMark(true);
        /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
         request.setPrintProgress(false);
        /*设置自定义上传进度回调 (必须继承 ProgressListener)*/
        request.setProgressListener(new PutObjectProgressListener());
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        //请求视频点播服务的请求ID
        log.info("RequestId=" + response.getRequestId());
        if (response.isSuccess()) {
            log.info("VideoId=" + response.getVideoId());
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            log.info("VideoId=" + response.getVideoId());
            log.info("ErrorCode=" + response.getCode());
            log.info("ErrorMessage=" + response.getMessage());
        }
        return response.getVideoId();
    }

    /**
     * 网络流上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param url
     */
    private void testUploadURLStream(String accessKeyId, String accessKeySecret, String title, String fileName, String url) {
        UploadURLStreamRequest request = new UploadURLStreamRequest(accessKeyId, accessKeySecret, title, fileName, url);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setShowWaterMark(true);
        /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
        request.setPrintProgress(true);
        /* 设置自定义上传进度回调 (必须继承 ProgressListener) */
        request.setProgressListener(new PutObjectProgressListener());
        request.setTemplateGroupId("0768723c32791f575e5f0b125100f1a5");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadURLStreamResponse response = uploader.uploadURLStream(request);
        //请求视频点播服务的请求ID
        log.info("RequestId=" + response.getRequestId());
        if (response.isSuccess()) {
            log.info("VideoId=" + response.getVideoId());
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            log.info("VideoId=" + response.getVideoId());
            log.info("ErrorCode=" + response.getCode());
            log.info("ErrorMessage=" + response.getMessage());
        }
    }

    /**
     * 文件流上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     */
    private void testUploadFileStream(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadFileStreamRequest request = new UploadFileStreamRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        //request.setShowWaterMark(true);
        /* 设置上传完成后的回调URL(可选)，建议通过点播控制台配置消息监听事件，参见文档 https://help.aliyun.com/document_detail/57029.html */
        //request.setCallback("http://callback.sample.com");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
//         request.setPrintProgress(true);
        /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
//        request.setProgressListener(new PutObjectProgressListener());
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadFileStreamResponse response = uploader.uploadFileStream(request);
        //请求视频点播服务的请求ID
        log.info("RequestId=" + response.getRequestId());
        if (response.isSuccess()) {
            log.info("VideoId=" + response.getVideoId());
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            log.info("VideoId=" + response.getVideoId());
            log.info("ErrorCode=" + response.getCode());
            log.info("ErrorMessage=" + response.getMessage());
        }
    }

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
        request.setTemplateGroupId(videoDto.getTemplateGroupId());
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
                e.printStackTrace();
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
     * 图片上传接口，本地文件上传示例
     * 参数参考文档 https://help.aliyun.com/document_detail/55619.html
     *
     * @param accessKeyId
     * @param accessKeySecret
     */
    private void testUploadImageLocalFile(String accessKeyId, String accessKeySecret) {
        /* 图片类型（必选）取值范围：default（默认)，cover（封面），watermark（水印）*/
        String imageType = "cover";
        UploadImageRequest request = new UploadImageRequest(accessKeyId, accessKeySecret, imageType);
        request.setImageType("cover");
        /* 图片文件扩展名（可选）取值范围：png，jpg，jpeg */
        //request.setImageExt("png");
        /* 图片标题（可选）长度不超过128个字节，UTF8编码 */
        //request.setTitle("图片标题");
        /* 图片标签（可选）单个标签不超过32字节，最多不超过16个标签，多个用逗号分隔，UTF8编码 */
        //request.setTags("标签1,标签2");
        /* 存储区域（可选）*/
        //request.setStorageLocation("out-4f3952f78c0211e8b3020013e7.oss-cn-shanghai.aliyuncs.com");
        /* 流式上传时，InputStream为必选，fileName为源文件名称，如:文件名称.png(可选)*/
        //request.setFileName("测试文件名称.png");
        /* 开启默认上传进度回调 */
        // request.setPrintProgress(true);
        /* 设置自定义上传进度回调 (必须继承 ProgressListener) */
        // request.setProgressListener(new PutObjectProgressListener());
        UploadImageImpl uploadImage = new UploadImageImpl();
        UploadImageResponse response = uploadImage.upload(request);
        log.info("RequestId=" + response.getRequestId());
        log.info("ErrorCode=" + response.getCode());
        log.info("ErrorMessage" + response.getMessage());
        log.info("ImageId=" + response.getImageId());
        log.info("ImageURL=" + response.getImageURL());

    }

    /**
     * 图片上传接口，流式上传示例（支持文件流和网络流）
     * 参数参考文档 https://help.aliyun.com/document_detail/55619.html
     *
     * @param accessKeyId
     * @param accessKeySecret
     */
    private void testUploadImageStream(String accessKeyId, String accessKeySecret) {
        String imageType = "cover";
        UploadImageRequest request = new UploadImageRequest(accessKeyId, accessKeySecret, imageType);
        /* 图片类型（必选）取值范围：default（默认)，cover（封面），watermark（水印）*/
        // request.setImageType(imageType);
        /* 图片文件扩展名（可选）取值范围：png，jpg，jpeg */
        //request.setImageExt("png");
        /* 图片标题（可选）长度不超过128个字节，UTF8编码 */
        //request.setTitle("图片标题");
        /* 图片标签（可选）单个标签不超过32字节，最多不超过16个标签，多个用逗号分隔，UTF8编码 */
        //request.setTags("标签1,标签2");
        /* 存储区域（可选）*/
        //request.setStorageLocation("out-4f3952f78c0211e8b3020013e7.oss-cn-shanghai.aliyuncs.com");
        /* 流式上传时，InputStream为必选，fileName为源文件名称，如:文件名称.png(可选)*/
        //request.setFileName("测试文件名称.png");
        /* 开启默认上传进度回调 */
        // request.setPrintProgress(true);
        /* 设置自定义上传进度回调 (必须继承 ProgressListener) */
        // request.setProgressListener(new PutObjectProgressListener());

        // 1.文件流上传
        // InputStream fileStream = getFileStream(request.getFileName());
        // if (fileStream != null) {
        //     request.setInputStream(fileStream);
        // }

        // 2.网络流上传
        // String url = "http://image.sample.com/sample.png";
        // InputStream urlStream = getUrlStream(url);
        // if (urlStream != null) {
        //     request.setInputStream(urlStream);
        // }

        // 开始上传图片
        UploadImageImpl uploadImage = new UploadImageImpl();
        UploadImageResponse response = uploadImage.upload(request);
        log.info("RequestId=" + response.getRequestId());
        log.info("ErrorCode=" + response.getCode());
        log.info("ErrorMessage" + response.getMessage());
        log.info("ImageId=" + response.getImageId());
        log.info("ImageURL=" + response.getImageURL());
    }




    /**
     * 获取视频信息
     * @param videoId 发送请求客户端
     * @return GetVideoInfoResponse 获取视频信息响应数据
     * @throws Exception
     */
    public static GetVideoInfoResponse getVideoInfo(String videoId) throws Exception {
        DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }



    /**
     * 获取视频列表
     * @param client 发送请求客户端
     * @return GetVideoListResponse 获取视频列表响应数据
     * @throws Exception
     */
    public static GetVideoListResponse getVideoList(DefaultAcsClient client) throws Exception {
        GetVideoListRequest request = new GetVideoListRequest();
        // 分别取一个月前、当前时间的UTC时间作为筛选视频列表的起止时间
        String monthAgoUTCTime = generateUTCTime(new Date(System.currentTimeMillis() - 150 * 86400L));
        String nowUTCTime = generateUTCTime(new Date(System.currentTimeMillis()));
        // 视频创建的起始时间，为UTC格式
        request.setStartTime(monthAgoUTCTime);
        // 视频创建的结束时间，为UTC格式
        request.setEndTime(nowUTCTime);
        // 视频状态，默认获取所有状态的视频，多个用逗号分隔
        // request.setStatus("Uploading,Normal,Transcoding");
        request.setPageNo(1);
        request.setPageSize(20);
        return client.getAcsResponse(request);
    }

    private InputStream getFileStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private InputStream getUrlStream(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
