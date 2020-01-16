package com.fantacg.user.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.pojo.worker.WorkerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname RedisReceiver
 * @Created by Dupengfei 2020-01-02 17:45
 * @Version 2.0
 */
@Service
public class WorkerInfoRedisReceiver {

    @Autowired
    WorkerInfoService workerInfoService;

    public void receiveMessage(String message) {
        System.out.println("消息来了：" + message);
        WorkerInfo workerInfo = JSON.parseObject(message, WorkerInfo.class);
        workerInfoService.addWorkerInfo(workerInfo);
    }

}
