package com.fantacg.answer.service;

import com.fantacg.common.dto.answer.AddQuickProjectTrainingDto;
import com.fantacg.common.utils.Result;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname QuickProjectTrainingService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface QuickProjectTrainingService {

    /**
     * 创建快速学习培训
     *
     * @param dto
     * @return
     */
    Result addQuickProjectTraining(AddQuickProjectTrainingDto dto);


    /**
     * 查询快速学习培训
     *
     * @param status
     * @return
     */
    Result queryQuickProjectTraining(Integer status);
}
