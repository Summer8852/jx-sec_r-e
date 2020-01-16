package com.fantacg.answer.mapper;

import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.dto.answer.PtDto;
import com.fantacg.common.pojo.answer.ProjectTraining;
import com.fantacg.common.vo.answer.ProjectTrainingVO;
import com.github.pagehelper.Page;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectTrainingMapper extends Mapper<ProjectTraining> {
    /**
     * 分页查询项目培训基本信息
     *
     * @param params
     * @return
     */
    Page<ProjectTraining> selectAllProjectTrainingByPage(Map<String, Object> params);

    /**
     * 查询当前用户当天最大的项目编号
     * @param memberId
     * @return
     */
    Long getSysNo(Long memberId);

    /**
     * 工人查询项目培训列表
     *
     * @param map
     * @return
     */
    Page<ProjectTrainingVO> queryWorkerTrainings(Map<String, Object> map);

    /**
     * 查询培训历史记录
     * @param dto
     * @return
     */
    Page<ProjectTrainingVO> queryWorkerTrainingDetailByCardNum(PtDto dto);

    /**
     * 查询培训答题详情
     * @param dto
     * @return
     */
    HashMap<String,Object> queryProjectTrainingAnswerDetail(PtDto dto);

    /**
     * 单个删除培训项目
     * @param dto
     * @return
     */
    int deleteProjectTraining(ProjectTrainingDto dto);

    /**
     * 结束项目培训考试
     * @param dto
     * @return
     */
    int stopProjectTraining(ProjectTrainingDto dto);

    /**
     * 提前开始项目培训培训
     * @param dto
     * @return
     */
    int startProjectTraining(ProjectTrainingDto dto);

    /**
     * 自动结束考试
     * @param trainingDate
     * @return
     */
    int voluntaryStopProjectTraining(@Param("trainingDate") String trainingDate);

    /**
     *  查询 小于 当前时间+ 30天 时间 已删除的培训
     * @param delDate
     * @return
     */
    List<ProjectTrainingDto> selectDelProjectTrainingByDelDate(@Param("delDate") String delDate);
}
