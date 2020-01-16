package com.fantacg.common.pojo.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTraining 项目培训基本信息数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "pb_project_training")
public class ProjectTraining implements Serializable {

    /**
     * （自增业务编号）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 培训编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sysNo;

    /**
     * 培训课程名称
     */
    private String trainingName;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 培训日期（yyyy-MM-dd）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date trainingDate;

    /**
     * 培训时长（单位：小时）
     */
    private String trainingDuration;

    /**
     * 培训类型(参考数据字典：培训类型字典表)
     */
    private String trainingTypeCode;

    /**
     * 培训人
     */
    private String trainer;

    /**
     * 培训机构
     */
    private String trainingOrg;

    /**
     * 培训地址
     */
    private String trainingAddress;

    /**
     * 培训简述
     */
    private String description;

    /**
     * 培训附件(参考附件表)
     */
    private String fileAttachmentInfo;

    /**
     * 培训状态  -1删除 0未开始 1进行中 2结束
     */
    private Integer trainingStatus;

    /**
     * 删除时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date delDate;

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

    /**
     * 编辑人 id
     */
    private Long editUserName;

    /**
     * 编辑时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editDate;

    /**
     * 培训机构名称
     */
    @Transient
    private String trainingOrgName;

    /**
     * 项目名称
     */
    @Transient
    private String projectName;

}
