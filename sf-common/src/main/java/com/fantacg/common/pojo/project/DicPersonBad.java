package com.fantacg.common.pojo.project;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DicPersonBad 人员不良行为字典表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "pb_dic_person_bad")
public class DicPersonBad implements Serializable {

    private Long id;
    /**
     * 行为类别
     */
    private String creditType;
    /**
     * 行为代码
     */
    private String creditCode;
    /**
     * 不良行为
     */
    private String credit;

    /**
     * 法律依据
     */
    private String law;
    /**
     * 处罚依据
     */
    private String punish;

}
