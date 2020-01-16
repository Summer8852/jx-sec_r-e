package com.fantacg.common.pojo.worker;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P> 人员实名信息数据表
 * @author 智慧安全云
 * @Classname WorkerInfo
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name="pb_worker_info")
public class WorkerInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件类型
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 工人性别
     */
    private String gender;

    /**
     * 民族
     */
    private String nation;

    /**
     * 学历
     */
    private String eduLevel;

    /**
     * 学位
     */
    private int degree;

    /**
     * 类别
     */
    private String workerType;

    /**
     * 出生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    /**
     * 籍贯
     */
    private String birthPlaceCode;

    /**
     * 住址
     */
    private String address;

    /**
     * 头像
     */
    private String headImageUrl;

    /**
     * 政治面貌
     */
    private String politicsType;

    /**
     * 是否加入工会
     */
    private int isJoined;

    /**
     * 加入工会时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinedTime;

    /**
     * 手机号码
     */
    private String cellPhone;

    /**
     * 文化程度
     */
    private String cultureLevelType;

    /**
     * 特长
     */
    private String specialty;

    /**
     * 是否有重大病史
     */
    private int hasBadMedicalHistory;

    /**
     * 紧急联系人姓名
     */
    private String urgentLinkMan;

    /**
     * 紧急联系电话
     */
    private String urgentLinkManPhone;

    /**
     * 工种
     */
    private String workTypeCode;

    /**
     * 当前聘用企业
     */
    private String workCorpName;

    /**
     * 开始工作日期
     */
    private Date workDate;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 发证机关
     */
    private String grantOrg;

    /**
     * 正面照 URL
     */
    private String positiveIdCardImageUrl;

    /**
     * 反面照 URL
     */
    private String negativeIdCardImageUrl;

    /**
     * 有效期开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 有效期结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryDate;

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

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeStart;

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeEnd;

    /**
     * 企业编号
     */
    @Transient
    private String corpCode;

    /**
     * 项目编码
     */
    @Transient
    private String projectCode;

    /**
     * 班组编号
     */
    @Transient
    private String teamSysNo;

    /**
     * 工人类型
     */
    @Transient
    private String workerRole;

    /**
     * 项目编码
     */
    @Transient
    private Long memberId;

    /**
     * 项目审批状态
     */
    @Transient
    private String approvalStatus;

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 项目审批状态
     */
    @Transient
    private String cardNum;
}
