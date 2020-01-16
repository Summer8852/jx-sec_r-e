package com.fantacg.answer.service.imp;

import com.alibaba.fastjson.JSON;
import com.fantacg.answer.mapper.AnswerLogMapper;
import com.fantacg.answer.mapper.ProjectTrainingDetailMapper;
import com.fantacg.answer.mapper.ProjectTrainingMemberMapper;
import com.fantacg.common.pojo.answer.Answer;
import com.fantacg.common.pojo.answer.AnswerLog;
import com.fantacg.common.pojo.answer.ProjectTrainingDetail;
import com.fantacg.common.pojo.answer.ProjectTrainingMember;
import com.fantacg.answer.service.AnswerLogService;
import com.fantacg.answer.service.AnswerService;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerLogServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class AnswerLogServiceImpl implements AnswerLogService {

    /**
     * 总分
     */
    private static final Integer TOTAL_SCORE = 100;
    /**
     * 及格分
     */
    private static final Integer PASS_SCORE = 60;
    @Autowired
    AnswerLogMapper answerLogMapper;
    @Autowired
    AnswerService answerService;
    @Autowired
    ProjectTrainingDetailMapper projectTrainingDetailMapper;
    @Autowired
    ProjectTrainingMemberMapper projectTrainingMemberMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 提交考试结果 并计算成绩
     *
     * @param answerLog
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAnswerLogs(AnswerLog answerLog) {
        //查询是否已经答题
        int num = this.isAnswerLog(answerLog.getProjectTrainingId(), answerLog.getTrainingSysNo(), answerLog.getIdCardNumber());
        if (num == 0) {
            // 添加答题记录
            answerLog.setId(new IdWorker().nextId());
            answerLog.setId(new IdWorker().nextId());
            answerLog.setInDate(new Date());
            answerLog.setIdCardNumber(AesEncrypt.decryptAES(answerLog.getIdCardNumber()));
            answerLog.setExactnessAnswer(JSON.toJSONString(answerLog.getList()));
            this.answerLogMapper.insertSelective(answerLog);
            //培训得分
            int score = 0;
            //平均分 总分/总题数
            float average = TOTAL_SCORE / answerLog.getAnswerNum();
            //正确题数
            int exactnessNum = 0;
            //判断题目
            int size = answerLog.getList().size();
            for (int i = 0; i < size; i++) {
                Map map = (Map) answerLog.getList().get(i);
                Iterator iterator = map.keySet().iterator();
                String key = String.valueOf(iterator.next());
                String value = String.valueOf(map.get(key));
                Answer answer = answerService.queryAnswerById(Long.valueOf(key));
                if (answer.getRightKey().equals(value)) {
                    exactnessNum += 1;
                    score += average;
                }
            }
        //全部正确满分
        if (exactnessNum == size) {
            score = 100;
        }
        //添加结算成绩
        ProjectTrainingDetail projectTrainingDetail = new ProjectTrainingDetail();
        projectTrainingDetail.setId(new IdWorker().nextId());
        projectTrainingDetail.setProjectTrainingId(answerLog.getProjectTrainingId());
        projectTrainingDetail.setTrainingSysNo(answerLog.getTrainingSysNo());
        projectTrainingDetail.setIdCardNumber(answerLog.getIdCardNumber());
        //判断是否及格
        if (score >= PASS_SCORE) {
            projectTrainingDetail.setIsPass(1);
        } else {
            projectTrainingDetail.setIsPass(0);
        }
        projectTrainingDetail.setScore(String.valueOf(score));
        projectTrainingDetail.setInDate(new Date());
        this.projectTrainingDetailMapper.insertSelective(projectTrainingDetail);

        //修改状态 是否已培训
        log.info("修改状态 是否已培训");
        ProjectTrainingMember ptm = new ProjectTrainingMember();
        ptm.setProjectTrainingId(answerLog.getProjectTrainingId());
        ptm.setTrainingSysNo(answerLog.getTrainingSysNo());
        ptm.setIdCardNumber(answerLog.getIdCardNumber());
        num = this.projectTrainingMemberMapper.updateProjectTrainingMember(ptm);
        if (num > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    } else

    {
        return Result.failure(ResultCode.REPEAT_ANSWER_EXCEPTION);
    }

}


    /**
     * 查询是否已经答题
     *
     * @param projectTrainingId 培训ID
     * @param trainingSysNo     培训编号
     * @param idCardNumber      身份证号 加密
     * @return
     */
    private Integer isAnswerLog(Long projectTrainingId, Long trainingSysNo, String idCardNumber) {
        Example example = new Example(ProjectTrainingMember.class);
        example.createCriteria()
                .andEqualTo("projectTrainingId", projectTrainingId)
                .andEqualTo("trainingSysNo", trainingSysNo)
                .andEqualTo("idCardNumber", AesEncrypt.decryptAES(idCardNumber))
                .andEqualTo("isTraining", 1);
        return projectTrainingMemberMapper.selectCountByExample(example);
    }

}

