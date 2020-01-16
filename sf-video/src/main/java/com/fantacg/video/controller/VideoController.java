package com.fantacg.video.controller;

import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.video.aspect.Requirespermissions;
import com.fantacg.common.pojo.video.VideoPalyLog;
import com.fantacg.video.service.VideoPalyLogService;
import com.fantacg.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    VideoPalyLogService videoPalyLogService;

    /**
     * 上传视频
     *
     * @param videoDto 视频信息
     * @param request request
     * @param videoFile 预览视频url
     * @return 上传成功/失败
     */
    @PostMapping("/upload")
    @Requirespermissions("video:upload")
    public Result upload(@Validated(QpGroup.Add.class) VideoDto videoDto, BindingResult result, HttpServletRequest request,
                         @RequestParam(value = "file") MultipartFile videoFile) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.videoService.upload(videoDto, videoFile, request);
    }

    /**
     * 视频列表 （平台管理员）
     * @return 返回视频列表
     */
    @GetMapping("/page")
    public Result queryVideoByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "searchCateId", required = false) String searchCateId) {
        return this.videoService.queryVideoByPage(page, rows, key, searchCateId);
    }


    /**
     * 视频中心
     *
     * @return 返回视频列表
     */
    @GetMapping("/index")
    public Result queryVideoIndexByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "searchCateId", required = false) String searchCateId) {
        return this.videoService.queryVideoIndexByPage(page, rows, key, searchCateId);
    }

    /**
     * 查询推荐的视频（根据用户查看的视频分类id 查询该分类的id）
     */
    @GetMapping("/rand/{cateId}")
    public Result queryVideoRand(@PathVariable("cateId") String cateId) {
        return this.videoService.selectVideoListRand(cateId);
    }

    /**
     * 创建培训（企业管理员） 视频列表
     *
     * @return 返回视频列表
     */
    @GetMapping("/vs")
    public Result queryVideoByPage() {
        return this.videoService.queryVideoList();
    }


    /**
     * 查询视频详情
     *
     * @param id 阿里云视频id
     * @return 返回视频详情
     */
    @GetMapping("/detail/{id}")
    public Result videoDetail(@PathVariable("id") String id) {
        return this.videoService.videoDetail(id);
    }

    /**
     * 视频上架
     *
     * @param videoId 阿里云视频id
     * @return 返回上架成功/失败
     */
    @PostMapping("/toshelf")
    public Result upload(@RequestBody List<String> videoId) {
        return this.videoService.toShelfVideo(videoId);
    }

    /**
     * 下架视频
     *
     * @param videoId 阿里云视频id
     * @return 返回下架成功/失败
     */
    @PostMapping("/undercarriage")
    public Result undercarriage(@RequestBody List<String> videoId) {
        return this.videoService.toUndercarriageVideo(videoId);
    }

    /**
     * 删除视频
     *
     * @param id 阿里云视频id
     * @return 返回删除成功/失败
     */
    @DeleteMapping("delete/{id}")
    @Requirespermissions("video:del")
    public Result delete(@PathVariable("id") String id) {
        return this.videoService.delete(id);
    }

    /**
     * 获取视频列表信息
     *
     * @param videoIds 视频id
     * @return 视频列表
     */
    @GetMapping("/getVideoByVideoId")
    public Result getVideoByVideoIds(@RequestParam("videoIds") List<String> videoIds) {
        return this.videoService.getVideoByVideoIds(videoIds);
    }


    /**
     * 获取播放视频凭证
     *
     * @param id 阿里云视频id
     * @return 返回播放凭证
     */
    @GetMapping("/getVideoPlayAuthResponse/{id}/{type}")
    public Result getVideoPlayAuthResponse(@PathVariable("id") String id, @PathVariable("type") Integer type) {
        return this.videoService.getVideoPlayAuthResponse(id, type);
    }


    /**
     * 添加播放记录（使用多少数据流量）
     * @param videoPalyLog
     * @return
     */
    @PostMapping("/log")
    public Result addVideoPalyLog(@RequestBody VideoPalyLog videoPalyLog){
        return this.videoPalyLogService.addVideoPalyLog(videoPalyLog);
    }


}
