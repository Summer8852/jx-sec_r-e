package com.fantacg.answer.service;

import com.fantacg.common.dto.answer.AddProjectTrainingDto;
import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.dto.answer.PtDto;
import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.utils.Result;
import com.fantacg.common.vo.answer.ReceiveVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface ProjectTrainingService {

    /**
     * 创建项目培训基本信息
     *
     * @param addProjectTrainingDto
     * @return
     */
    Result addProjectTraining(AddProjectTrainingDto addProjectTrainingDto);

    /**
     * 分页查询项目培训基本信息
     * @param page 分页
     * @param rows 分页
     * @param sortBy 排序
     * @param desc 排序
     * @param searchProjectCode 培训项目编码搜索
     * @param searchTrainingDate 培训日期搜索
     * @param searchTrainingName 课程名称搜索
     * @param trainingStatus 培训状态
     * @param searchTypeCode 培训类型搜索
     * @return
     */
    Result queryProjectTrainingByPage(Integer page, Integer rows, String sortBy, Boolean desc, String searchProjectCode, Date searchTrainingDate, String searchTrainingName, Integer trainingStatus, String searchTypeCode);

    /**
     * 获取项目培训编号
     *
     * @return
     */
    Result getSysNo();

    /**
     * 工人查询项目培训列表
     *
     * @param map
     * @return
     */
    Result queryWorkerTrainings(Map<String, Object> map);

    /**
     * 查询工人项目培训观看视频
     *
     * @param dto
     * @return
     */
    Result queryWorkerTrainingVideos(PtDto dto);

    /**
     * 查询工人项目培训的试题
     *
     * @param dto
     * @return
     */
    Result queryWorkerTrainingAnswers(PtDto dto);

    /**
     * 工人扫码 查询 项目培训的试题
     *
     * @param dto
     * @return
     */
    Result queryWorkerTrainingAnswersQR(PtDto dto);

    /**
     * 单个删除项目培训
     *
     * @param dto
     * @return
     */
    Result deleteProjectTraining(ProjectTrainingDto dto);

    /**
     * 结束项目培训考试
     *
     * @param dto
     * @return
     */
    Result stopProjectTraining(ProjectTrainingDto dto);

    /**
     * 查看项目培训考试详情
     *
     * @param dto
     * @return
     */
    Result detailProjectTraining(ProjectTrainingDto dto);

    /**
     * 提前开始项目培训培训
     *
     * @param dto
     * @return
     */
    Result startProjectTraining(ProjectTrainingDto dto);

    /**
     * 自动结束考试
     */
    void voluntaryStopProjectTraining(String trainingDate);

    /**
     * （历史记录）分页查询项目培训记录
     *
     * @return
     */
    Result queryWorkerTrainingDetailByCardNum(PtDto dto);

    /**
     * 答题详情（已答题）
     *
     * @param dto
     * @return
     */
    Result queryProjectTrainingAnswerDetail(PtDto dto);

    /**
     * 自动删除培训 （保存30天）
     */
    void voluntaryDelProjectTraining();


    /**
     * 接收上报的安全教育数据
     * @param str
     * @return
     */
    Result receiveSafetyEducation(PlatformKey platformKey);

}
