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
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PayRollDetail 项目参建单位-班组-人员-工资单明细数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class PayRollDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工资单编码(关联工资单表中的工资单编码)
     */
    private Long payRollCode;

    /**
     * 工人姓名
     */
    private String workerName;

    /**
     * 证件类型(参考数据字典：人员证件类型字典表)
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 出勤天数
     */
    private Integer days;

    /**
     * 总工时(单位：小时)
     */
    private String workHours;

    /**
     * 工人工资卡号
     */
    private String payRollBankCardNumber;

    /**
     * 工人工资卡银行代码
     */
    private String payRollBankCode;

    /**
     * 工人工资卡开户行名称
     */
    private String payRollBankName;

    /**
     * 工资代发银行卡号
     */
    private String payBankCardNumber;

    /**
     * 工资代发银行代码
     */
    private String payBankCode;

    /**
     * 工资代发开户行名称
     */
    private String payBankName;

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
