package com.fantacg.common.pojo.worker;

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
 * @Classname PersonRegisterChange 人员注册变更数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class PersonRegisterChange implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工人姓名
     */
    private String PersonName;

    /**
     * 证件类型
     */
    private String IDCardType;

    /**
     * 证件号码
     */
    private String IDCardNumber;

    /**
     * 原所在企业统一社会信用代码
     */
    private String OldCorpCode;

    /**
     * 原所在企业名称
     */
    private String OldCorpName;

    /**
     * 原执业印章号
     */
    private String OldStampNum;

    /**
     * 现所在企业统一社会信用代码
     */
    private String CorpCode;

    /**
     * 现所在企业名称
     */
    private String CorpName;

    /**
     * 现执业印章号
     */
    private String newStampNum;

    /**
     * 变更时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ChangeDate;


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

}
