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
 * @Classname PersonGoodCreditInfo 人员良好行为信息数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_person_good_credit_info")
@Data
public class PersonGoodCreditInfo implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 姓名
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
     * 登记人
     */
    private String createUserName;
    /**
     * 登记日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 良好行为描述
     */
    private String content;
    /**
     * 良好行为发生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date happenDate;
    /**
     * 良好行为发生地行政区划
     */
    private String regionNo;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 奖励部门
     */
    private String awardDepartMent;
    /**
     * 奖励部门级别 （认定部门级别字典表）
     */
    private String awardDepartLevel;
    /**
     * 奖励决定内容
     */
    private String mark;

    /**
     * 奖励决定文号
     */
    private String fileNum;

    /**
     * 奖励日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date praiseDate;

    /**
     * 是否删除 1删除
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
