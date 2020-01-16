package com.fantacg.common.dto.answer;

import com.fantacg.common.pojo.answer.QuickProjectTraining;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AddQuickProjectTrainingDto
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddQuickProjectTrainingDto {

    /**
     * 项目培训基本信息
     */
    private QuickProjectTraining qpt;

    /**
     * 项目培训题目信息
     */
    private List<Long> qptas;
    
}
