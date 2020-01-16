package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectInfo 项目基本信息数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_project_info")
@Data
public class ProjectInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "id不能为空", groups = QpGroup.Update.class)
    private Long id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目编码=6位行政区码+6位当前日期+2位分类编码+4位序列码
     * 项目编码
     */
    private String code;

    /**
     * 总承包 所属我的企业id (数据库文档没有 根据实际业务添加)
     */
    private String contractorCorpId;

    /**
     * 总承包单位统一社会信用代码
     */
    private String contractorCorpCode;

    /**
     * 总承包单位名称
     */
    private String contractorCorpName;

    /**
     * 项目简介
     */
    private String description;

    /**
     * 项目分类
     */
    private String category;

    /**
     * 建设单位名称
     */
    private String buildCorpName;

    /**
     * 建设单位统一社会信用代码
     */
    private String buildCorpCode;

    /**
     * 建设单位 所属我的企业id 数据库文档没有 根据实际业务添加)
     */
    private String buildCorpId;

    /**
     * 建设用地规划许可证编号
     */
    private String buildPlanNum;

    /**
     * 建设工程规划许可证编号
     */
    private String prjPlanNum;

    /**
     * 项目所在地
     */
    private String areaCode;

    /**
     * 总投资（万元）
     */
    private String invest;

    /**
     * 总面积 （平方米）
     */
    private String buildingArea;

    /**
     * 总长度（米）
     */
    private String buildingLength;

    /**
     * 开工日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 竣工日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date completeDate;

    /**
     * 联系人姓名
     */
    private String linkMan;

    /**
     * 联系人电话
     */
    private String linkPhone;

    /**
     * 项目状态 000 表示已删除
     */
    private String prjStatus;

    /**
     * 经度
     */
    private double lng;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 项目地址
     */
    private String address;

    /**
     * 立项文号
     */
    private String approvalNum;

    /**
     * 立项级别
     */
    private String approvalLevelNum;

    /**
     * 建设规模
     */
    private String prjSize;

    /**
     * 建设性质
     */
    private String propertyNum;

    /**
     * 工程用途
     */
    private String prjNum;

    /**
     * 国籍或地区
     */
    private String nationNum;

    /**
     * 第三方项目编码
     */
    private String thirdPartyProjectCode;

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
     * 项目分类名
     */
    @Transient
    private String categoryName;
    /**
     * 项目所在地
     */
    @Transient
    private String areaName;

    /**
     * 项目状态名
     */
    @Transient
    private String statusName;

    /**
     * 立项级别
     */
    @Transient
    private String approvalLevelName;
    /**
     * 建设规模
     */
    @Transient
    private String prjSizeName;

    /**
     * 建设性质
     */
    @Transient
    private String propertyName;

    /**
     * 工程用途
     */
    @Transient
    private String prjNumName;

    /**
     * 国籍或地区
     */
    @Transient
    private String nationName;


}
