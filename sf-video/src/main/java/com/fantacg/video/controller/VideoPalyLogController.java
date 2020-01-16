package com.fantacg.video.controller;

import com.fantacg.common.pojo.video.VideoPalyLog;
import com.fantacg.common.utils.Result;
import com.fantacg.video.service.VideoPalyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoPalyLogController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/videoPalyLog")
public class VideoPalyLogController {

    @Autowired
    VideoPalyLogService videoPalyLogService;

    /**
     * 添加播放记录（使用多少数据流量）
     *
     * @param videoPalyLog
     * @return
     */
    @PostMapping
    public Result addVideoPalyLog(@RequestBody VideoPalyLog videoPalyLog) {
        return this.videoPalyLogService.addVideoPalyLog(videoPalyLog);
    }

    /**
     * 查询播放记录
     */
    @GetMapping("/page")
    public Result queryVideoByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return this.videoPalyLogService.queryVideoByPalyLogPage(page, rows);
    }


}
