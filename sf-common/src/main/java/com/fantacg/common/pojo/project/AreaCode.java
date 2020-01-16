package com.fantacg.common.pojo.project;

import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AreaCode  行政区域表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "pb_area_code")
public class AreaCode implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 行政区域编码
     */
    private String code;

    /**
     * 行政区域名称
     */
    private String name;

    /**
     * 行政等级 0 省 1市 2区/县
     */
    private String level;

    /**
     * 父级编码
     */
    private String parentCode;

    @Transient
    private List<AreaCode> children;


}

