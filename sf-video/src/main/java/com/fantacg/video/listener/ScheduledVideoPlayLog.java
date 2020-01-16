package com.fantacg.video.listener;

import cn.hutool.core.date.DateUtil;
import com.fantacg.video.service.VideoPalyLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 * <p>
 * Configuration      // 1.主要用于标记配置类，兼备Component的效果。
 * EnableScheduling   // 2.开启定时任务
 *
 * @author 智慧安全云
 * @Classname ScheduledVideoPlay
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
public class ScheduledVideoPlayLog {


    @Autowired
    VideoPalyLogService videoPalyLogService;

    /**
     * 自动结算播放记录
     * 两分钟执行一次：0/2 * * * * ?
     * 每天半夜12点30分执行一次：0 30 0 * * ?
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void voluntaryStopProjectTraining() {
        log.info("开始-自动结算:" + DateUtil.now());
        videoPalyLogService.videoPlayLogConsume(10);
        log.info("结束-自动结算");
    }


}
