package com.fantacg.common.pojo.project;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpEmployee  企业职员数据表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class CorpEmployee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 企业统一社会信用代码,班组所在所属企业统一社会信用代码，如果无统一社会信用代码，则填写组织机构代码
     */
    private String corpCode;

    /**
     * 企业名称,班组所在企业名称
     */
    private String corpName;

    /**
     * 员工姓名
     */
    private String workerName;

    /**
     * 证件类型,参考数据字典：人员证件类型字典表
     */
    private String idCardType;

    /**
     * 证件号码
     */
    private String idCardNumber;

    /**
     * 岗位类型,参见数据字典：岗位类型字典表
     */
    private Long jobType;

    /**
     * 状态,参考数据字典：职员状态字典表
     */
    private Long status;

    /**
     * 创建人
     */
    private String inUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inDate;

    /**
     * 编辑人
     */
    private String editUserName;

    /**
     * 编辑时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editDate;

}
