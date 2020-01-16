package com.fantacg.answer.mapper;

import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.pojo.answer.ProjectTrainingDetail;
import com.fantacg.common.pojo.answer.ProjectTrainingVideo;
import com.fantacg.common.vo.answer.FileAttachmentInfoVO;
import com.fantacg.common.vo.answer.ProjectTrainingDetailVO;
import com.github.pagehelper.Page;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingDetailMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectTrainingDetailMapper extends Mapper<ProjectTrainingDetail> {

    /**
     * 查询项目培训详情
     *
     * @param dto
     * @return
     */
    List<ProjectTrainingDetailVO> queryProjectTrainingDetails(ProjectTrainingDto dto);

    /**
     * 查询单次培训附件培训附件
     *
     * @param dto
     * @return 返回培训附件列表
     */
    List<FileAttachmentInfoVO> queryFileAttachmentInfos(ProjectTrainingDto dto);

    /**
     * 分页查询培训人在当前账号下的培训详情
     *
     * @param memberId     项目管理员 id
     * @param idCardNumber 培训人身份证号
     * @return 返回培训记录
     */
    Page<ProjectTrainingDetailVO> queryProjectTrainingDetailsByCardId(
            @Param("memberId") Long memberId,
            @Param("idCardNumber") String idCardNumber);

    /**
     * 添加
     * @param list
     * @return
     */
    int insertProjectTrainingDetails(@Param("list") List<ProjectTrainingDetail> list);
}
