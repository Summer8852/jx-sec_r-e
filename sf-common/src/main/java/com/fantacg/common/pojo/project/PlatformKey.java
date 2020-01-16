package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;



/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Equipment 用户外接设备标识表 （用于外接设备的）
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_platform_key")
public class PlatformKey implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 设备编号
     */
    @NotEmpty(message = "编号不能为空", groups = QpGroup.Add.class)
    private String platformNo;

    /**
     * 备注（对接平台）
     */
    @NotEmpty(message = "备注不能为空", groups = QpGroup.Add.class)
    private String platformName;

    /**
     * key
     */
    private String accessKey;

    /**
     * 账号id
     */
    @NotNull(message = "账号id不能为空", groups = QpGroup.Add.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    /**
     * 项目编号
     */
    @NotEmpty(message = "项目编号不能为空", groups = QpGroup.Add.class)
    private String projectCode;

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
     * 是否删除 1删除
     */
    private Integer isDel;

}
