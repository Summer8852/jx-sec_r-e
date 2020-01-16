package com.fantacg.common.pojo.user;


import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Worker 普通用户表(工人)
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_worker")
public class Worker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "参数不能为空", groups = {QpGroup.Del.class})
    private Long id;

    /**
     * 用户名
     */
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确", groups = {QpGroup.Add.class})
    @NotEmpty(message = "手机号码不能为空", groups = {QpGroup.Add.class})
    private String workerName;

    /**
     * 密码
     */
    @Length(min = 4, max = 30, message = "密码只能在4~30位之间", groups = {QpGroup.Add.class})
    @NotEmpty(message = "密码不能为空", groups = {QpGroup.Add.class})
    private String password;

    /**
     * 密码的盐值
     */
    @JsonIgnore
    private String salt;

    /**
     * 电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证正面图片
     */
    @NotEmpty(message = "身份证正面图片不能为空", groups = {QpGroup.realNameAuth.class})
    private String realNameImgFront;

    /**
     * 身份证反面图片
     */
    @NotEmpty(message = "身份证反面图片不能为空", groups = {QpGroup.realNameAuth.class})
    private String realNameImgBack;

    /**
     * 拍照图片
     */
    @NotEmpty(message = "手握身份证图片不能为空", groups = {QpGroup.realNameAuth.class})
    private String realNameImgPerson;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否实人认证 默认0  1认证
     */
    private Integer isAuth;

    @Transient
    private String code;

    @Transient
    private String registerType;

}
