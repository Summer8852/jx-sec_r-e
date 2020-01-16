package com.fantacg.common.pojo.worker;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P> 人员注册专业数据表
 *
 * @author 智慧安全云
 * @Classname PersonRegisterProfession
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class PersonRegisterProfession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工人姓名
     */
    private String personName;

    /**
     * 证件类型 （人员证件类型字典表）
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 注册类型及等级 （注册类型及等级字典表）
     */
    private String registerType;

    /**
     * 注册专业编号
     */
    private String professionId;

    /**
     * 专业名称
     */
    private String professionName;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;

    /**
     * 截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    /**
     * 创建人
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
     * 编辑人
     */
    private Long editUserName;

    /**
     * 编辑时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editDate;
}
