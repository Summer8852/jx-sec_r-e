package com.fantacg.common.pojo.project;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 下载文档模板管理
 *
 * @author DUPENGFEI
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Word 下载文档模板管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "tb_word")
@Data
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 文件名
     */
    private String wordName;

    /**
     * 文件path;
     */

    private String wordPath;

    /**
     * 文件类型 doc xls;
     */
    private String wordType;

    /**
     * 文件模板唯一id
     */
    private String wordNo;

    /**
     * 模板文件示例图
     */
    private String wordImg;

    /**
     * 是否 通用 （通用不用指定账号）
     */
    private Integer isCommon;

    /**
     * 模板所属账号 id
     */
    private Long accountId;

    /**
     * 添加人id
     */
    private Long intUserName;

    /**
     * 时间
     */
    private Date inDate;



}
