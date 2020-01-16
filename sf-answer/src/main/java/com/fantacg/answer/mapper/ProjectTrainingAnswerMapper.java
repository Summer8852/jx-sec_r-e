package com.fantacg.answer.mapper;

import com.fantacg.common.pojo.answer.Answer;
import com.fantacg.common.pojo.answer.ProjectTrainingAnswer;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingAnswerMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectTrainingAnswerMapper extends Mapper<ProjectTrainingAnswer> {

    /**
     * 查询工人项目培训的试题
     *
     * @param map
     * @return
     */
    List<Answer> queryPTAByPTId(Map<String, Object> map);

    /**
     * 添加培训试题
     *
     * @param list
     * @return
     */
    int insertProjectTrainingAnswer(@Param("list") List<ProjectTrainingAnswer> list);
}
