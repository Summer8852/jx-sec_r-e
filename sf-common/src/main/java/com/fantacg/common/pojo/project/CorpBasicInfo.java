package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpBasicInfo 企业基本信息表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_corp_basic_info")
@Data
public class CorpBasicInfo implements Serializable {

    @NotNull(message = "id不能为空", groups = {QpGroup.Del.class})
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 统一社会信用代码,如果无统一社会信用代码，则填写组织机构代码
     */
    @NotEmpty(message = "统一社会信用代码不能为空")
    private String corpCode;

    /**
     * 企业名称
     */
    @NotEmpty(message = "企业名称不能为空")
    private String corpName;

    /**
     * 企业登记注册类型,参考数据字典：企业登记注册类型字典表
     */
    private String corpType;

    /**
     * 工商营业执照注册号
     */
    private String licenseNum;

    /**
     * 注册地区编码,参考数据字典：行政区划字典表
     */
    private String areaCode;

    /**
     * 企业营业地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 法定代表人姓名
     */
    private String legalMan;

    /**
     * 法定代表人职务
     */
    private String legalManDuty;

    /**
     * 法定代表人职称
     */
    private String legalManProTitle;

    /**
     * 法定代表人证件类型,参考数据字典：人员证件类型字典表
     */
    private String legalManIdCardType;

    /**
     * 法定代表人证件号码
     */
    private String legalManIdCardNumber;

    /**
     * 注册资本,单位：万元
     */
    private double regCapital;

    /**
     * 实收资本,单位：万元
     */
    private double factRegCapital;

    /**
     * 资本币种,参考数据字典：币种字典表
     */
    private String capitalCurrencyType;

    /**
     * 注册日期,精确到天,格式：yyyy-MM-dd
     */
    private Date registerDate;

    /**
     * 成立日期,精确到天,格式：yyyy-MM-dd
     */
    private Date establishDate;

    /**
     * 办公电话
     */
    private String officePhone;

    /**
     * 传真号码
     */
    private String faxNumber;

    /**
     * 联系人姓名
     */
    private String linkMan;

    /**
     * 联系人电话
     */
    private String linkPhone;

    /**
     * 企业邮箱
     */
    private String email;

    /**
     * 企业网址
     */
    private String webSite;

    /**
     * 创建人
     */
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

    /**
     * 是否删除
     */
    private Integer isDel;


    /**
     * 企业登记注册类型名
     */
    @Transient
    private String corpTypeName;

    /**
     * 注册地区编码名
     */
    @Transient
    private String areaCodeName;
    /**
     * 法定代表人证件类型民
     */
    private String legalManIdCardName;
    /**
     * 资本币种名
     */
    private String capitalCurrencyName;


}
