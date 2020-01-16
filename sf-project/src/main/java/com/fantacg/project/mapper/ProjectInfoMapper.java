package com.fantacg.project.mapper;

import com.fantacg.common.dto.project.ProjectVo;
import com.fantacg.common.pojo.project.ProjectInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectInfoMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectInfoMapper extends Mapper<ProjectInfo> {

    List<ProjectInfo> selectAllProjectListByPage(Map<String, Object> params);

    int selectProjectInfByCode(Long memberId, String code);

    ProjectInfo selectProjectListById(Long id);

    String selectProjectCodeById(Long id);

    String selectThirdPartyProjectCodeMax();

    int updateProjectInfo(ProjectInfo projectInfo);

    /**
     * 查询项目编号和项目名称
     * @param memberId
     * @return
     */
    List<ProjectVo> selectProjectCodeAll(Long memberId);



}
