package com.fantacg.project.mapper;

import com.fantacg.common.dto.project.ProjectWorkerDTO;
import com.fantacg.common.dto.project.ProjectWorkerVO;
import com.fantacg.common.dto.project.PtmVO;
import com.fantacg.common.pojo.project.ProjectWorker;
import com.github.pagehelper.Page;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectWorkerMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectWorkerMapper extends Mapper<ProjectWorker> {

    /**
     * 筛选班组-人员实名信息列表
     *
     * @param dto
     * @return
     */
    Page<ProjectWorkerVO> screeningProjectWorker(ProjectWorkerDTO dto);

    /**
     * 筛选班组-人员实名信息列表(创建项目)
     *
     * @param dto
     * @return
     */
    List<PtmVO> screeningProjectWorkerByTraining(ProjectWorkerDTO dto);


    /**
     * 批量或单个删除班组人员信息
     *
     * @param lists
     * @return
     * @Param("ids") List<Long> lists,@Param("memberId") Long memberId
     */
    int delProjectWorker(@Param("ids") List<Long> lists, @Param("memberId") Long memberId);

    /**
     * 分页查询班组人员信息列表
     *
     * @param params
     * @return
     */
    Page<ProjectWorkerVO> queryProjectWorkerBySysNoPage(HashMap<String, Object> params);


    /**
     *
     * @param list
     * @return
     */
    int addEmpsBatch(@Param("list") List<ProjectWorker> list);

    /**
     * 修改班组组长状态
     *
     * @param teamSysNo    班组编号
     * @param idCardNumber 身份号
     * @param isTeamLeader 是/否 班组组长
     * @return 返回是否修改成功
     */
    int updateIsTeamLeader(
            @Param("teamSysNo") String teamSysNo,
            @Param("idCardNumber") String idCardNumber,
            @Param("isTeamLeader") Integer isTeamLeader);
}
