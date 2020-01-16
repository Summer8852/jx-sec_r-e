package com.fantacg.answer.service.imp;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.dto.answer.AddQuickProjectTrainingDto;
import com.fantacg.answer.filter.LoginInterceptor;
import com.fantacg.answer.mapper.QuickProjectTrainingAnswerMapper;
import com.fantacg.answer.mapper.QuickProjectTrainingMapper;
import com.fantacg.common.pojo.answer.QuickProjectTraining;
import com.fantacg.common.pojo.answer.QuickProjectTrainingAnswer;
import com.fantacg.answer.service.QuickProjectTrainingService;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname QuickProjectTrainingServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class QuickProjectTrainingServiceImpl implements QuickProjectTrainingService {

    @Autowired
    QuickProjectTrainingMapper quickProjectTrainingMapper;
    @Autowired
    QuickProjectTrainingAnswerMapper quickProjectTrainingAnswerMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 创建快速学习
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addQuickProjectTraining(AddQuickProjectTrainingDto dto) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        //删除redis 缓存数据
        redisTemplate.delete(KeyConstant.QUICKPROJECTTRAINING_MEMBER_LIST_PREFIX + memberId);
        //创建培训
        QuickProjectTraining qpt = dto.getQpt();
        qpt.setId(new IdWorker().nextId());
        qpt.setInUserName(memberId);
        qpt.setInDate(new Date());
        int i = this.quickProjectTrainingMapper.insertSelective(qpt);
        //添加题目
        QuickProjectTrainingAnswer qpta = new QuickProjectTrainingAnswer();
        qpta.setId(new IdWorker().nextId());
        qpta.setInUserName(memberId);
        qpta.setInDate(new Date());
        for (Long answerId : dto.getQptas()) {
            qpta.setAnswerId(answerId);
            i = this.quickProjectTrainingAnswerMapper.insertSelective(qpta);
        }
        if (i > 0) {
            return Result.success(ResultCode.DATA_FOUND_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_FOUND_ERROR);
    }

    /**
     * 查询快速学习列表
     *
     * @return
     */
    @Override
    public Result queryQuickProjectTraining(Integer status) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        List<QuickProjectTraining> quickProjectTrainings = this.queryQuickProjectTrainingMemberId(memberId, status);
        return Result.success(quickProjectTrainings);

    }

    /**
     * 用户id 查询快速学习列表(不同状态)
     */
    private List<QuickProjectTraining> queryQuickProjectTrainingMemberId(Long memberId, Integer status) {
        try {
            if (memberId != null) {
                //查询缓存
                String s = redisTemplate.opsForValue().get(KeyConstant.QUICKPROJECTTRAINING_MEMBER_LIST_PREFIX);
                if (StringUtils.isNotEmpty(s)) {
                    List<QuickProjectTraining> quickProjectTrainings = objectMapper.readValue(s, List.class);
                    return quickProjectTrainings;
                }
                //判断是否传入状态 未传入 查询所有状态
                String sql = "  in_user_name = " + memberId;
                if (status != null) {
                    sql += " and training_status = " + status;
                }
                Example example = new Example(QuickProjectTraining.class);
                example.createCriteria().andCondition(sql);
                //查询数据库
                List<QuickProjectTraining> quickProjectTrainings = this.quickProjectTrainingMapper.selectByExample(example);
                if (!quickProjectTrainings.isEmpty()) {
                    redisTemplate.opsForValue().set(KeyConstant.QUICKPROJECTTRAINING_MEMBER_LIST_PREFIX + memberId, JSON.toJSONString(quickProjectTrainings));
                    return quickProjectTrainings;
                }
            }
            return null;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }


}
