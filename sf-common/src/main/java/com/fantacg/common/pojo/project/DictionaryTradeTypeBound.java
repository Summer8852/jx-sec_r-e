package com.fantacg.common.pojo.project;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DictionaryTradeTypeBound 专业类别字典表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_dic_trade_type_bound")
@Data
public class DictionaryTradeTypeBound implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * 数据值
     */
    @NotNull(message = "数据值不能为空", groups = QpGroup.Add.class)
    private String value;

    /**
     * 标签名
     */
    @NotNull(message = "标签名不能为空", groups = QpGroup.Add.class)
    private String label;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空", groups = QpGroup.Add.class)
    private String type;

    /**
     * 描述
     */
    @NotNull(message = "描述不能为空", groups = QpGroup.Add.class)
    private String description;

    /**
     * 排序 （升序）
     */
    @NotNull(message = "排序不能为空", groups = QpGroup.Add.class)
    private Double sort;

    /**
     * 父级编号
     */
    private String parentId;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 删除标记
     */
    private Integer delFlag;


    @Transient
    private String name;
}
