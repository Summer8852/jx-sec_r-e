package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname Platform 设备/平台表
 * @Created by Dupengfei 2020-01-15 14:31
 * @Version 2.0
 */
@Data
@Table(name = "tb_platform")
public class Platform implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty(message = "编号不能为空", groups = QpGroup.Del.class)
    private String platformNo;

    /**
     * 设备/平台名称
     */
    @NotEmpty(message = "名称不能为空", groups = QpGroup.Add.class)
    private String platformName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inDate;
}
