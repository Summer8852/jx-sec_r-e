package com.fantacg.answer.mapper;

import com.fantacg.common.pojo.answer.ProjectTrainingMember;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingMemberMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectTrainingMemberMapper extends Mapper<ProjectTrainingMember> {

    /**
     * 添加参与培训人员
     * @param list
     * @return
     */
    int insertProjectTrainingMember(@Param("list") List<ProjectTrainingMember> list);

    /**
     *  删除 参与培训人员
     * @param ptm
     * @return
     */
    int updateProjectTrainingMember(ProjectTrainingMember ptm);
}
