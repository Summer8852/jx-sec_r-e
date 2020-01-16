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
 * @Classname PersonCredential 人员资质数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class PersonCredential implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工人姓名
     */
    private String name;

    /**
     * 证件类型 （人员证件类型字典表）
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 证件种类 （人员证书种类字典表）
     */
    private String certificationCategoriesType;

    /**
     * 证书类型 (人员证书类型相关字典表)
     */
    private String certificationType;

    /**
     * 证书类型名
     */
    private String certificationTypeName;

    /**
     * 证书等级名称 （人员资质等级相关字典表）
     */
    private String credentialLevelType;

    /**
     * 证书名称
     */
    private String certificationName;

    /**
     * 证书编号
     */
    private String certificationCode;

    /**
     * 认定部门
     */
    private String confirmOrganization;

    /**
     * 岗位类型 (岗位类型字典表)
     */
    private String jobType;

    /**
     * 岗位名称
     */
    private String jobTitle;

    /**
     * 第一次发证时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date firstBeginDate;

    /**
     * 证书有效时间（起）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validBeginDate;

    /**
     * 证书有效时间（止）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date validEndDate;

    /**
     * 发证机关
     */
    private String grantOrg;

    /**
     * 工作单位
     */
    private String workCorpName;


    /**
     * 资质证书状态 (资质证书状态字典表)
     */
    private String certificationStatus;


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
