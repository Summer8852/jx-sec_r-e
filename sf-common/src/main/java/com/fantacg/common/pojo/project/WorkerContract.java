package com.fantacg.common.pojo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WorkerContract 项目参建单位-班组-人员-劳动合同数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class WorkerContract implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属企业统一社会信用代码(如果无统一社会信用代码，则填写组织机构代码)
     */
    private String corpCode;

    /**
     * 所属企业名称
     */
    private String corpName;

    /**
     * 证件类型（人员证件类型字典表）
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 合同编号（工人劳动合同编号）
     */
    private String contractCode;

    /**
     * 合同期限类型（合同期限类型字典表）
     */
    private String contractPeriodType;

    /**
     * 开始日期 （固定期限合同是需要填写： yyyy-MM-DD）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;


    /**
     * 结束时期（固定期限合同是需要填写： yyyy-MM-DD）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 计量单价(根据结算方式，对应的单价，单位：元)
     */
    private BigDecimal unitPrice;

    /**
     * 合同附件	(附件表)
     */
    private String fileAttachmentInfo;


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
