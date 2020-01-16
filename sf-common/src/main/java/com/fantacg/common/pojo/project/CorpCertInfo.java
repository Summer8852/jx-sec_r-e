package com.fantacg.common.pojo.project;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpCertInfo 企业资质数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_corp_cert_info")
@Data
public class CorpCertInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 统一社会信用代码，如果无统一社会信用代码，则填写组织机构代码
     */
    private String corpCode;

    /**
     * 企业名称
     */
    private String corpName;

    /**
     * 资质资格类型，参考数据字典：企业资质资格类型字典表
     */
    private String certTypeNum;

    /**
     * 证书编号
     */
    private String certId;

    /**
     * 专业类别，参考企业资质资格专业类别字典表
     */
    private String tradeBoundNum;


    /**
     * 专业子项
     */
    private String tradeTypeBoundChildMark;

    /**
     * 资质资格等级，参考企业资质等级字典表
     */
    private String titleLevelNum;

    /**
     * 批准资质资格内容，该资质的打印内容
     */
    private String mark;

    /**
     * 资质资格限定内容
     */
    private String limitContent;

    /**
     * 首次批准资质资格文号
     */
    private String noteNumber;

    /**
     * 首次批准资质资格日期
     */
    private Date noteDate;

    /**
     * 资质资格取得方式，参考企业资质取得方式字典表
     */
    private String addTypeNum;

    /**
     * 资质资格状态，参考企业资质状态字典
     */
    private String certTradeStatusNum;

    /**
     * 资质资格状态变更时间
     */
    private Date certTradeModifyDate;

    /**
     * 资质资格状态变更原因，资质资格降级、暂扣、注销的原因
     */
    private String certTradeModifyMark;

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


    /**
     * 资质资格类型名
     */
    @Transient
    private String certTypeName;
    /**
     * 专业类别名
     */
    @Transient
    private String tradeBoundName;

    /**
     * 资质资格等级名
     */
    @Transient
    private String titleLevelName;


    /**
     * 资质资格取得方式名
     */
    @Transient
    private String addTypeName;

    /**
     * 资质资格状态名
     */
    @Transient
    private String certTradeStatusName;

}
