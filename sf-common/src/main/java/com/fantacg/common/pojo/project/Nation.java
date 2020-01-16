package com.fantacg.common.pojo.project;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Nation 世界各国和地区名称代码 （GB/T 2659-2000)
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name = "pb_nation")
@Data
public class Nation implements Serializable {

    private Long id;

    /**
     * 中文简称
     */
    private String chineseName;

    /**
     * 英文简称
     */
    private String englishName;

    /**
     * 两字符代码
     */
    private String twoCode;

    /**
     * 三字符代码
     */
    private String threeCode;

    /**
     * 数字代码
     */
    private String numCode;

    /**
     * 中文和英文全称
     */
    private String chineseEnglishName;
}
