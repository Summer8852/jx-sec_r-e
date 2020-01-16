package com.fantacg.answer.service;

import com.fantacg.common.pojo.answer.AnswerLog;
import com.fantacg.common.utils.Result;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerLogService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface AnswerLogService {

    /**
     * 添加答题记录 （题目答完一起提交）
     * @param answerLog
     * @return
     */
    Result addAnswerLogs(AnswerLog answerLog);

}
