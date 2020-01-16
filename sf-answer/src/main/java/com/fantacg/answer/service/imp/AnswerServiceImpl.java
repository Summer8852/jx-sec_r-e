package com.fantacg.answer.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fantacg.answer.mapper.AnswerMapper;
import com.fantacg.common.pojo.answer.Answer;
import com.fantacg.answer.service.AnswerService;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerServiceImpl 考试题目
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 根据工种查询考试试题
     *
     * @param workType
     * @return
     */
    @Override
    public Result queryAnswerListByWorkType(String workType) {
        try {
            List<Answer> answers;
            String s = redisTemplate.opsForValue().get(KeyConstant.ANSWER_WORKTYPE_KEY_PREFIX + workType);
            if (StringUtil.isNotEmpty(s)) {
                answers = objectMapper.readValue(s, List.class);
                log.info("查询工种考试题目(Redis)- " + workType + ":" + answers);
                return Result.success(answers);
            }

            answers = this.answerMapper.queryAnswerListByWorkType(workType);

            if (!answers.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.ANSWER_WORKTYPE_KEY_PREFIX + workType, objectMapper.writeValueAsString(answers));
            }
            log.info("查询工种考试题目:" + workType + ":" + answers);
            return Result.success(answers);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 根据试题Id 查询题目详细
     *
     * @param answerId
     * @return
     */
    @Override
    public Answer queryAnswerById(Long answerId) {
        try {
            Answer answer = new Answer();
            String s = redisTemplate.opsForValue().get(KeyConstant.ANSWER_ID_KEY_PREFIX + answerId);
            if (StringUtil.isNotEmpty(s)) {
                answer = objectMapper.readValue(s, Answer.class);
                return answer;
            }
            answer.setAnswerId(answerId);
            answer = answerMapper.selectOne(answer);
            if (answer != null) {
                redisTemplate.opsForValue().set(KeyConstant.ANSWER_ID_KEY_PREFIX + answerId, JSON.toJSONString(answer));
                return answer;
            }
            return null;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }


    /**
     * 添加试题
     *
     * @param answer
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAnswer(Answer answer) {

        int i = answerMapper.addAnswer(answer);
        if (i > 0) {
            redisTemplate.delete(redisTemplate.keys(KeyConstant.ANSWER_WORKTYPE_KEY_PREFIX + "*"));
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 删除试题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delAnswer(List<Integer> answerIds) {

        int i = answerMapper.delAnswer(answerIds);
        if (i > 0) {
            redisTemplate.delete(redisTemplate.keys(KeyConstant.ANSWER_WORKTYPE_KEY_PREFIX + "*"));
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

    /**
     * 修改题目
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateAnswer(Answer answer) {

        int i = answerMapper.updateAnswer(answer);

        if (i > 0) {
            redisTemplate.delete(redisTemplate.keys(KeyConstant.ANSWER_WORKTYPE_KEY_PREFIX + "*"));
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 根据视频id 查询题目列表
     *
     * @param list
     * @return
     */
    @Override
    public Result queryAnswerVideoById(List<String> list) {
        List<Answer> answers = new ArrayList<>();
        for (String s : list) {
            List<Answer> answerList = this.answerList(s);
            if (!answerList.isEmpty()) {
                for (Answer answer : answerList) {
                    answers.add(answer);
                }
            }
        }
        return Result.success(answers);
    }


    private List<Answer> answerList(String videoId) {
        try {
            List<Answer> answers;
            String s = redisTemplate.opsForValue().get(KeyConstant.ANSWER_ID_KEY_PREFIX + videoId);
            if (StringUtil.isNotEmpty(s)) {
                answers = objectMapper.readValue(s, new TypeReference<List<Answer>>() {
                });
                return answers;
            }
            answers = this.answerMapper.queryAnswerListByVideoId(videoId);
            redisTemplate.opsForValue().set(KeyConstant.ANSWER_ID_KEY_PREFIX + videoId, JSON.toJSONString(answers));
            return answers;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

}
