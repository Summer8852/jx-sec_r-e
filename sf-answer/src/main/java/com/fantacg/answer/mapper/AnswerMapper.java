package com.fantacg.answer.mapper;

import com.fantacg.common.pojo.answer.Answer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface AnswerMapper extends Mapper<Answer> {

    /**
     * 根据工种查询试题列表
     *
     * @param workType
     * @return
     */
    List<Answer> queryAnswerListByWorkType(String workType);

    /**
     * 添加试题
     *
     * @param answer
     * @return
     */
    int addAnswer(Answer answer);

    /**
     * 删除试题
     *
     * @param answerId
     * @return
     */
    int delAnswer(@Param("answerId") List<Integer> answerId);

    /**
     * 修改试题
     *
     * @param answer
     * @return
     */
    int updateAnswer(Answer answer);

    /**
     * 根据视频id查询试题
     *
     * @param videoId
     * @return
     */
    List<Answer> queryAnswerListByVideoId(@Param("videoId") String videoId);
}
