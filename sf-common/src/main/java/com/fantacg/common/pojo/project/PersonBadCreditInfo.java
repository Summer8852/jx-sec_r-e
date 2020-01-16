package com.fantacg.common.pojo.project;

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
 * @Classname PersonBadCreditInfo  人员不良行为信息数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_person_bad_credit_info")
@Data
public class PersonBadCreditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 姓名
     */
    private String personName;

    /**
     * 证件类型
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 所在企业统一社会信用代码
     */
    private String corpCode;

    /**
     * 所在企业名称
     */
    private String corpName;

    /**
     * 登记部门
     */
    private String createDepName;

    /**
     * 登记人姓名
     */
    private String createUserName;

    /**
     * 登记日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 不良行为类别
     */
    private String creditType;

    /**
     * 不良行为代码
     */
    private String creditCode;

    /**
     * 不良行为描述
     */
    private String content;

    /**
     * 不良行为发生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date happenDate;

    /**
     * 不良行为发生地行政区划
     */
    private String regionNo;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 处罚部门
     */
    private String departName;

    /**
     * 处罚部门级别
     */
    private String departTypeId;

    /**
     * 处罚依据
     */
    private String punishEvidence;

    /**
     * 处罚决定内容
     */
    private String mark;

    /**
     * 处罚决定文号
     */
    private String fileNum;

    /**
     * 处罚日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date punishDate;

    /**
     * 处罚截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date punishEDate;

    /**
     * 是否删除 1 删除
     */
    private Integer isDel;

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
