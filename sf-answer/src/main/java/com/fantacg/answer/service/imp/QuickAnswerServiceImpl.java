package com.fantacg.answer.service.imp;

import com.alibaba.fastjson.JSON;
import com.fantacg.answer.mapper.QuickAnswerMapper;
import com.fantacg.common.pojo.answer.QuickAnswer;
import com.fantacg.answer.service.QuickAnswerService;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname QuickAnswerServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class QuickAnswerServiceImpl implements QuickAnswerService {

    @Autowired
    QuickAnswerMapper quickAnswerMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 查询全部
     * @return
     */
    @Override
    public Result queryQuickAnswerAll() {
        List<QuickAnswer> quickAnswers = this.queryQuickAnswers();
        return Result.success(quickAnswers);
    }

    /**
     * 查询随机
     * @return
     */
    @Override
    public Result queryQuickAnswerRandom(Integer integer) {
        List<QuickAnswer> quickAnswers = this.queryQuickAnswers();
        Collections.shuffle(quickAnswers);
        List<QuickAnswer> quickAnswers1 = quickAnswers.subList(0, integer);
        return Result.success(quickAnswers1);
    }


    /**
     * 查询全部
     * @return
     */
    private List<QuickAnswer> queryQuickAnswers() {
        try {
            //查询redis 缓存数据
            String s = redisTemplate.opsForValue().get(KeyConstant.QUICKANSWER_LIST_KEY_PREFIX);
            //是否存在数据 存在则返回数据
            if (StringUtil.isNotEmpty(s)) {
                List<QuickAnswer> quickAnswers = objectMapper.readValue(s, List.class);
                return quickAnswers;
            }
            //查询数据库数据
            List<QuickAnswer> quickAnswers = this.quickAnswerMapper.selectAll();
            //数据加入redis缓存
            if (!quickAnswers.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.QUICKANSWER_LIST_KEY_PREFIX, JSON.toJSONString(quickAnswers));
                return quickAnswers;
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


}
