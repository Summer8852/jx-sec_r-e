package com.fantacg.answer.scheduled;

import cn.hutool.core.date.DateUtil;
import com.fantacg.answer.service.ProjectTrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname ScheduledProjectTraining
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Component
public class ScheduledProjectTraining {


    @Autowired
    ProjectTrainingService projectTrainingService;

    /**
     * 自动结束培训
     * 每天凌晨1点执行一次：0 0 1 * * ?
     * 每天半夜12点30分执行一次：0 30 0 * * ?
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void voluntaryStopProjectTraining() {
        log.info("开始-自动结束培训");
        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
        projectTrainingService.voluntaryStopProjectTraining(date);
        log.info("结束-自动结束培训");
    }


    /**
     * 自动删除培训 （保留30天）
     * 每天凌晨1点执行一次：0 0 1 * * ?
     * 每天半夜12点30分执行一次：0 30 0 * * ?
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void voluntaryDelProjectTraining() {
        log.info("自动删除培训 （保留30天）");
        projectTrainingService.voluntaryDelProjectTraining();
    }

}
