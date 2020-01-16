package com.fantacg.video.listener;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.constant.MQConstant;
import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoListener
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Component
@Slf4j
public class VideoListener {

    @Autowired
    private VideoService  videoService;

    @RabbitListener(queues = MQConstant.VIDEO_DEAD_QUEUE_NAME)
    public void listenDelete(String params) {
        log.info(params + ":开始消费\n");
        VideoDto videoDto = (VideoDto) JSON.parse(params);
        videoService.addVideo(videoDto);
        log.info("结束消费");
    }

}
