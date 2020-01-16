package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpBasicCert 企业基本信息信息与企业资质数据关联表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_corp_basic_cert")
@Data
@ToString
public class CorpBasicCert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id不能为空", groups = {QpGroup.Del.class})
    private Long id;

    /**
     * 企业信息 id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long corpBasicId;

    /**
     * 企业资质数据
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private String corpCertId;

    /**
     * 创建人id
     */
    private Long inMemberId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inDate;

}
