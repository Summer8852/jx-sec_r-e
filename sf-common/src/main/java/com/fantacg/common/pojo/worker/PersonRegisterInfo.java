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
 * <P>
 * @author 智慧安全云
 * @Classname PersonRegisterInfo 人员注册信息数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class PersonRegisterInfo implements Serializable {

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
     * 学历 （学历类型字典表）
     */
    private String eduLevel;

    /**
     * 学位 （学位类型字典表）
     */
    private String degree;

    /**
     * 注册类型及等级 （注册类型及等级字典表）
     */
    private String registerType;

    /**
     * 注册证书编号
     */
    private String certificationCode;

    /**
     * 注册有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectDate;

    /**
     * 发证单位
     */
    private String awardDepart;

    /**
     * 发证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date awardDate;

    /**
     * 执业印章号
     */
    private String stampNum;

    /**
     * 所在企业行业类型 （注册人员所在企业的行业类型字典表）
     */
    private String callingType;

    /**
     * 所在企业组织机构代码 （如果无统一社会信用代码，则填写组织机构代码）
     */
    private String corpCode;

    /**
     * 所在企业名称
     */
    private String CorpName;

    /**
     * 所在企业证书编号
     */
    private String CorpCertID;

    /**
     * 注册专业1 （注册监理工程师才有）
     */
    private String Regspec1;

    /**
     * 注册专业2 （注册监理工程师才有）
     */
    private String Regspec2;

    /**
     * 执业资格状态 （注册人员执业资格状态字典表）
     */
    private String QState;

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
