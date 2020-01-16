package com.fantacg.common.vo.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingDetailVO
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTrainingDetailVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 培训id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectTrainingId;

    /**
     * 培训编号
     */
    private String trainingSysNo;

    /**
     * 培训课程名称
     */
    private String trainingName;

    /**
     * 身份证号
     */
    private String idCardNumber;

    /**
     * 是否及格
     */
    private Integer isPass;

    /**
     * 分数
     */
    private String score;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long inUserName;

}
