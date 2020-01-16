package com.fantacg.upload.listener;

import com.fantacg.common.constant.MQConstant;
import com.fantacg.common.utils.Result;
import com.fantacg.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname UploadListener
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Component
@Slf4j
public class UploadListener {

    @Autowired
    private UploadService uploadService;

    /**
     * 身份证阅读器图片上传
     */
    @RabbitListener(queues = MQConstant.UPLOAD_DEAD_QUEUE_NAME)
    public void process(String params) throws IOException {
        log.info(params + "：身份证阅读器图片上传开始");
        ConcurrentHashMap<Object, String> map = new ConcurrentHashMap<>();
        map.put("path",params);
        Result result = this.uploadService.uploadDelImage(map);
        log.info("身份证阅读器图片上传结束！:" + result.getData());
    }


}

