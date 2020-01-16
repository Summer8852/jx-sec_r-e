package com.fantacg.common.pojo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Classname BankCardInfo 银行卡信息表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_bank_card_info")
@Data
public class BankCardInfo implements Serializable {


    private String id;

    /**
     * 附件类型
     */
    private String businessType;

    /**
     * 业务编号
     */
    private String businessSysNo;

    /**
     * 银行支行名称
     */
    private String bankName;

    /**
     * 银行账户
     */
    private String bankNumber;

    /**
     * 银行联号
     */
    private String bankLinkNumber;


    /**
     * 创建人
     */
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
