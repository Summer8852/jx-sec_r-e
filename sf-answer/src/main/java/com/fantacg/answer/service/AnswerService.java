package com.fantacg.answer.service;

import com.fantacg.common.pojo.answer.Answer;
import com.fantacg.common.utils.Result;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface AnswerService  {

    /**
     * 工种查询考试试题
     * @param workType
     * @return
     */
    Result queryAnswerListByWorkType(String workType);

    /**
     * 根据试题Id 查询题目详细
     * @param answerId
     * @return
     */
    Answer queryAnswerById(Long answerId);


    /**
     * 添加试题
     * @param answer
     * @return
     */
    Result addAnswer(Answer answer);

    /**
     * 删除试题
     * @param answerIds
     * @return
     */
    Result delAnswer(List<Integer> answerIds);

    /**
     * 修改试题
     * @param answer
     * @return
     */
    Result updateAnswer(Answer answer);

    /**
     * 根据视频id 查询题目列表
     * @param list
     * @return
     */
    Result queryAnswerVideoById(List<String> list);

}
