package com.fantacg.common.pojo.project;


import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname FileAttachmentInfo 附件数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_file_attachment_info")
@Data
public class FileAttachmentInfo implements Serializable {


    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 附件类型
     */
    @NotEmpty(message = "附件类型不能为空", groups = {QpGroup.Add.class})
    private String businessType;

    /**
     * 业务编号
     */
    @NotEmpty(message = "业务编号不能为空", groups = {QpGroup.Add.class})
    private String businessSysNo;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 附件路径
     */
    @NotEmpty(message = "附件路径不能为空", groups = {QpGroup.Add.class})
    private String url;

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

    /**
     * 是否删除 0否  1是
     */
    private Integer isDel;

    @Transient
    private String businessTypeName;


}
