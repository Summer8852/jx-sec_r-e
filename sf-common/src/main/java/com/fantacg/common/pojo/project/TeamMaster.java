package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname TeamMaster 项目参建单位-班组数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_team_master")
@Data
public class TeamMaster implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty(message = "id不能为空", groups = QpGroup.Update.class)
    private String id;


    /**
     * 班组编号,班组编号，系统自增业务编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private String teamSysNo;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 班组所在企业统一社会信用代码,如果无统一社会信用代码，则填写组织机构代码
     */
    private String corpCode;

    /**
     * 班组所在企业名称
     */
    private String corpName;

    /**
     * 班组名称,同一个项目上的班组名称不能重复
     */
    private String teamName;

    /**
     * 班组长姓名
     */
    private String teamLeaderName;

    /**
     * 班组长联系电话
     */
    private String teamLeaderPhone;

    /**
     * 班组长证件类型,参考数据字典：人员证件类型字典表
     */
    private String teamLeaderIdCardType;

    /**
     * 班组长证件号码
     */
    private String teamLeaderIdNumber;

    /**
     * 责任人姓名,班组所在企业负责人
     */
    private String responsiblePersonName;

    /**
     * 责任人联系电话
     */
    private String responsiblePersonPhone;

    /**
     * 责任人证件类型,参考数据字典：人员证件类型字典表
     */
    private String responsiblePersonIdCardType;

    /**
     * 责任人证件号码
     */
    private String responsiblePersonIdNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 进场日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date entryTime;

    /**
     * 退场日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date exitTime;

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

    /**
     * 是否删除 1删除  0 默认
     */
    private Integer isDel;
    /**
     * 参考数据标准：附件
     */
    @Transient
    private List<FileAttachmentInfo> fileAttachmentInfo;

    /**
     * 班组长证件类型名
     */
    @Transient
    private String teamLeaderIdCardName;

    /**
     * 责任人证件类型名
     */
    @Transient
    private String responsiblePersonIdCardName;

    /**
     * 第三方项目编码
     */
    @Transient
    private String projectId;

    /**
     * 项目名称
     */
    @Transient
    private String projectName;

    /**
     * 企业id
     */
    @Transient
    private String corpId;
}
