package com.fantacg.project.mapper;

import com.github.pagehelper.Page;
import com.fantacg.common.pojo.project.TeamMaster;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname TeamMasterMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface TeamMasterMapper extends Mapper<TeamMaster> {

    int installTeamMasters(TeamMaster teamMaster);

    TeamMaster selectTeamMasterById(String id);

    String selectTeamSysNoMax();

    Page<TeamMaster> selectTeamMasterByPage(Map<String, Object> params);

    List<TeamMaster> selectTeamMasterByList(List<Long> list);

    int updateTeamMasterIsDel(Map<String, Object> params);

    List<TeamMaster> selectTeamMasterByProjectId(String projectId, Long memberId);

    String selectByName(Long memberId, String teamName);
}
