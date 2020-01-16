package com.fantacg.video.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;

import java.net.URLEncoder;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoMediaUtil 媒资管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class VideoMediaUtil {
    /**
     * 搜索媒资信息
     *
     * @return SearchMediaResponse 搜索媒资信息响应数据
     * @throws Exception
     */
    public static SearchMediaResponse searchMedia() throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        SearchMediaRequest request = new SearchMediaRequest();
        request.setFields("Title,CoverURL,Status");
        request.setMatch("Status in ('Normal','Checking') and CreationTime = ('2018-07-01T08:00:00Z','2018-08-01T08:00:00Z')");
        request.setPageNo(1);
        request.setPageSize(10);
        request.setSearchType("video");
        request.setSortBy("CreationTime:Desc");
        return client.getAcsResponse(request);
    }


    /**
     * 获取视频信息
     *
     * @param videoId 视频id
     * @return GetVideoInfoResponse 获取视频信息响应数据
     * @throws Exception
     */
    public static GetVideoInfoResponse getVideoInfo(String videoId) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }


    /**
     * 批量获取视频信息函数
     *
     * @param "VideoId1,VideoId2" 多个以逗号分开
     * @return GetVideoInfosResponse 获取视频信息响应数据
     * @throws Exception
     */
    public static GetVideoInfosResponse getVideoInfos(String videoId) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        GetVideoInfosRequest request = new GetVideoInfosRequest();
        request.setVideoIds(videoId);
        return client.getAcsResponse(request);
    }


    /**
     * 修改视频信息
     *
     * @param request 需要需改的内容
     * @return UpdateVideoInfoResponse 修改视频信息响应数据
     * @throws Exception
     */
    public static UpdateVideoInfoResponse updateVideoInfo(UpdateVideoInfoRequest request) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        return client.getAcsResponse(request);
    }

    /**
     * 删除视频
     *
     * @param "VideoId1,VideoId2" 多个以逗号分开
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public static DeleteVideoResponse deleteVideo(String videoId) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoId);
        return client.getAcsResponse(request);
    }

    /**
     * 获取源文件信息
     *
     * @param videoId 发送请求客户端
     * @return GetMezzanineInfoResponse 获取源文件信息响应数据
     * @throws Exception
     */
    public static GetMezzanineInfoResponse getMezzanineInfo(String videoId) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        GetMezzanineInfoRequest request = new GetMezzanineInfoRequest();
        request.setVideoId(videoId);
        //源片下载地址过期时间
        request.setAuthTimeout(3600L);
        return client.getAcsResponse(request);
    }

    /**
     * 删除媒体流函数
     *
     * @param jobIds "JobId1,JobId2"
     * @return DeleteMezzaninesResponse 删除媒体流响应数据
     * @throws Exception
     */
    public static DeleteStreamResponse deleteStream(String videoId, String jobIds) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        DeleteStreamRequest request = new DeleteStreamRequest();
        request.setVideoId(videoId);
        request.setJobIds(jobIds);
        return client.getAcsResponse(request);
    }


    /**
     * 批量删除源文件函数
     * "VideoId1,VideoId2"
     *
     * @return DeleteMezzaninesResponse 批量删除源文件响应数据
     * @throws Exception
     */
    public static DeleteMezzaninesResponse deleteMezzanines(String videoIds) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        DeleteMezzaninesRequest request = new DeleteMezzaninesRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoIds);
        request.setForce(false);
        return client.getAcsResponse(request);
    }

    /**
     * 获取图片信息函数
     *
     * @return GetImageInfoResponse 获取图片信息响应数据
     * @throws Exception
     */
    public static GetImageInfoResponse getImageInfo(String imageId) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();
        GetImageInfoRequest request = new GetImageInfoRequest();
        request.setImageId(imageId);
        return client.getAcsResponse(request);
    }


    /**
     * 删除图片函数
     *
     * @param client 发送请求客户端
     * @return DeleteImageResponse 删除图片响应数据
     * @throws Exception
     */
    public static DeleteImageResponse deleteImage(DefaultAcsClient client) throws Exception {
        DeleteImageRequest request = new DeleteImageRequest();
        //根据ImageURL删除图片文件
        request.setDeleteImageType("ImageURL");
        String url = "http://sample.aliyun.com/cover.jpg";
        String encodeUrl = URLEncoder.encode(url, "UTF-8");
        request.setImageURLs(encodeUrl);
        //根据ImageId删除图片文件
        //request.setDeleteImageType("ImageId");
        //request.setImageIds("ImageId1,ImageId2");
        //根据VideoId删除指定ImageType的图片文件
        //request.setDeleteImageType("VideoId");
        //request.setVideoId("VideoId");
        //request.setImageType("SpriteSnapshot");
        return client.getAcsResponse(request);
    }


}
