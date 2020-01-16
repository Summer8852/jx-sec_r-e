package com.fantacg.common.pojo.answer;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 题目训练记录
 *
 * @author DUPENGFEI
 */

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerLog 题目训练记录
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_answer_log")
public class AnswerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 培训id(关联培训基本信息表中的ID)
     */
    @NotNull(message = "无项目培训id", groups = {QpGroup.Add.class})
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectTrainingId;

    /**
     * 培训编号(关联培训基本信息表中的培训编号)
     */
    @NotNull(message = "无培训编号", groups = {QpGroup.Add.class})
    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainingSysNo;

    /**
     * 题目id（关联题目表中的题目id）
     */
    private Long answerId;

    /**
     * 所选答案
     */
    private String exactnessAnswer;

    /**
     * 身份证
     */
    @NotEmpty(message = "无身份证", groups = {QpGroup.Add.class})
    private String idCardNumber;

    /**
     * 创建人 id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inDate;

    @Transient
    private List list;

    @Transient
    private Integer answerNum;
}
