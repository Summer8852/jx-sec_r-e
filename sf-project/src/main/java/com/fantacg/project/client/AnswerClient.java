package com.fantacg.project.client;


import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.utils.Result;
import com.fantacg.common.vo.answer.ReceiveVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 智慧安全云
 * @Classname AnswerClient
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@FeignClient(value = "answer-service")
public interface AnswerClient {


    @PostMapping("/wtraining/safetyEducation")
    Result safetyEducation(@RequestBody PlatformKey platformKey);
}

