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
 * @Classname ProjectWorker 项目参建单位-班组-人员数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_project_worker")
@Data
public class ProjectWorker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 企业统一社会信用代码
     */
    private String corpCode;

    /**
     * 企业名称
     */
    private String corpName;

    /**
     * 班组编号
     */
    private String teamSysNo;

    /**
     * 班组名称
     */
    private String teamName;

    /**
     * 工人姓名
     */
    private String workerName;

    /**
     * 是否班组长
     */
    private Integer isTeamLeader;

    /**
     * 证件类型
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 工种
     */
    private String workType;

    /**
     * 工人类型
     */
    private String workerRole;

    /**
     * 进场时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date entryTime;

    /**
     * 退场时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exitTime;

    /**
     * 进场确认附件
     */
    private String entryAttachmentUrl;

    /**
     * 退场确认附件
     */
    private String exitAttachmentUrl;

    /**
     * 制卡时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueCardDate;

    /**
     * 制卡采集照片
     */
    private String issueCardPicUrl;

    /**
     * 考勤卡号
     */
    private String cardNumber;

    /**
     * 发放工资银行卡号
     */
    private String payRollBankCardNumber;

    /**
     * 发放工资银行名称
     */
    private String payRollBankName;

    /**
     * 发放工资银行名称
     */
    private String payRollTopBankName;

    /**
     * 工资卡银行联号
     */
    private String bankLinkNumber;

    /**
     * 工资卡银行代码
     */
    private String payRollTopBankCode;

    /**
     * 是否有劳动合同
     */
    private String hasContract;

    /**
     * 有无购买工伤或意外伤害保险
     */
    private String hasBuyInsurance;

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
