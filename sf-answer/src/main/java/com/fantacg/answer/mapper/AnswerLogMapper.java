package com.fantacg.answer.mapper;

import com.fantacg.common.pojo.answer.AnswerLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerLogMapper 答题记录DAO
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface AnswerLogMapper extends Mapper<AnswerLog> {

    /**
     * 批量添加答题记录
     * @param list
     * @return
     */
    int insertanswerLogs(List<AnswerLog> list);

}
