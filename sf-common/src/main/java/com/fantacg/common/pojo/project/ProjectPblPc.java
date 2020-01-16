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
 * @Classname ProjectPblPc 项目基本信息 + 项目施工许可证   +  项目参建单位信息 关联表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_project_pbl_pc")
@Data
public class ProjectPblPc implements Serializable {

    private Long id;

    /**
     * 项目基本信息数据表 id
     */
    private String projectInfoId;

    /**
     * 项目施工许可证数据表 id
     */
    private Long pblId;

    /**
     * 项目参建单位信息数据表 id
     */
    private String pcId;

    /**
     * 项目参建单位-班组数据表 id
     */
    private String tmId;

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
