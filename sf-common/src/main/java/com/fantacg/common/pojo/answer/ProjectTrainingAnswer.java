package com.fantacg.common.pojo.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingAnswer 培训试题详情
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_project_training_answer")
public class ProjectTrainingAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 培训id(关联培训基本信息表中的ID)
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectTrainingId;

    /**
     * 培训编号(关联培训基本信息表中的培训编号)
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainingSysNo;
    /**
     * 试题id
     */
    private Long answerId;


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
}
