package com.fantacg.common.dto.answer;

import com.fantacg.common.pojo.answer.*;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.pojo.worker.WorkerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname safetyEducation 安全教育数据
 * @Created by Dupengfei 2019-12-20 15:04
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyEducation {


    private PlatformKey platformKey;
    /**
     * 项目培训基本信息
     */
    private ProjectTraining projectTraining;

    /**
     *项目培训视频信息
     */
    private List<String> videoIds;

    /**
     * 项目培训题目信息
     */
    private List<Long> answerIds;

    /**
     * 答题详情
     */
    private List<AnswerLog> answerLogs;

    /**
     * 答题结果
     */
    private List<ProjectTrainingDetail> projectTrainingDetails;

    /**
     * 实人信息
     */
    private List<WorkerInfo> workerInfos;




}
