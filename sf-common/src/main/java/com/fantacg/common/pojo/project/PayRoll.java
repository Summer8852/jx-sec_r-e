package com.fantacg.common.pojo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PayRoll 项目参建单位-班组-人员-工资单数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "pb_pay_roll")
public class PayRoll implements Serializable {


    /**
     * 工资单编号（自增业务编号）
     */
    private Long sysNo;

    /**
     * 工资单编码（参考编码规则：工资单编码）
     */
    private String payRollCode;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 所属企业统一社会信用代码（如果无统一社会信用代码，则填写组织机构代码）
     */
    private String corpCode;

    /**
     * 所属企业名称
     */
    private String corpName;

    /**
     * 班组编号
     */
    private String teamSysNo;

    /**
     * 发放工资的年月（格式：yyyy-MM）
     */
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date payMonth;

    /**
     * 附件（参考附件表）
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
