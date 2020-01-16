package com.fantacg.common.pojo.project;


import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectCorpInfo 项目参建单位信息数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_project_corp_info")
@Data
public class ProjectCorpInfo implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id不能为空", groups = QpGroup.Update.class)
    private Long id;


    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 参建企业id
     */
    private String corpId;
    /**
     * 参建企业统一社会信用代码,如果无统一社会信用代码，则填写组织机构代码
     */
    private String corpCode;

    /**
     * 参建企业名称
     */
    private String corpName;

    /**
     * 参建企业类型,参考数据字典：参建单位类型
     */
    private String corpType;

    /**
     * 进场时间
     */
    private Date entryTime;

    /**
     * 退场时间
     */
    private Date exitTime;

    /**
     * 项目经理名称
     */
    private String pmName;

    /**
     * 项目经理证件类型,参考数据字典：人员证件类型字典表
     */
    private String pmIdCardType;


    /**
     * 项目经理证件号码
     */
    private String pmIdCardNumber;

    /**
     * 项目经理手机号码
     */
    private String pmPhone;

    /**
     * 参考数据表：银行卡信息表
     */
    private String bankCardInfo;

    /**
     * 创建人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inUserName;

    /**
     * 创建时间
     */
    private Date inDate;

    /**
     * 编辑人
     */
    private Long editUserName;

    /**
     * 编辑时间
     */
    private Date editDate;


    /**
     * 项目经理证件类型名
     */
    @Transient
    private String pmIdCardName;

    /********************* 银行卡信息表**************************/
    /**
     * 业务类型
     */
    @Transient
    private String businessType;

    /**
     * 业务编号
     */
    @Transient
    private String businessSysNo;

    /**
     * 银行支行名称
     */
    @Transient
    private String bankName;

    /**
     * 银行账户
     */
    @Transient
    private String bankNumber;

    /**
     * 银行联号
     */
    @Transient
    private String bankLinkNumber;

    /**
     * 银行卡业务类型
     */
    @Transient
    private String businessTypeName;

}
