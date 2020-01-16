package com.fantacg.common.dto.answer;

import com.fantacg.common.utils.QpGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PtDto
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PtDto {
    /**
     * 项目培训Id
     */
    @NotEmpty(message = "无项目培训Id", groups = {QpGroup.Add.class})
    private String projectTrainingId;

    /**
     *项目培训编号
     */
    @NotEmpty(message = "无项目培训编号", groups = {QpGroup.Add.class})
    private String trainingSysNo;

    /**
     * 项目培训编号
     */
    private String idCardNumber;

    /**
     * 分页使用
     */
    private Integer page;
    private Integer rows;
}
