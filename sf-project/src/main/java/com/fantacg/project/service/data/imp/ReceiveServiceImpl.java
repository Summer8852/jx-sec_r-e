package com.fantacg.project.service.data.imp;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.common.vo.answer.ReceiveVO;
import com.fantacg.project.client.AnswerClient;
import com.fantacg.project.mapper.PlatformKeyMapper;
import com.fantacg.project.service.data.ReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname ReceiveServiceImpl
 * @Created by Dupengfei 2020-01-16 14:37
 * @Version 2.0
 */
@Slf4j
@Service
public class ReceiveServiceImpl implements ReceiveService {
    @Autowired
    PlatformKeyMapper platformKeyMapper;
    @Autowired
    AnswerClient answerClient;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result receiveSafetyEducation(SafetyEducation safetyEducation, HttpServletRequest request) {
        // 获取请求头信息
        String authorization= request.getHeader("Authorization");
        if(StringUtil.isEmpty(authorization)){
            return Result.success(ResultCode.PARAMETER_ERROR);
        }

        //获取设备信息
        Example example = new Example(PlatformKey.class);
        example.createCriteria()
                .andEqualTo("accessKey", authorization)
                .andEqualTo("isDel", 0);
        PlatformKey platformKey = this.platformKeyMapper.selectOneByExample(example);
        if (platformKey.getId() == null) {
            return Result.failure("该设备信息不存在,无法上报数据！");
        }
        answerClient.safetyEducation(platformKey);
        redisTemplate.opsForValue().set("safetyEducation:" + authorization,JSON.toJSONString(safetyEducation));
        Result result = answerClient.safetyEducation(platformKey);
        System.out.println(request);
        return result;
    }
}
