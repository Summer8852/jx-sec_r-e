package com.fantacg.common.pojo.user;

import com.fantacg.common.utils.QpGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Member 用户表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_member_info")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "参数不能为空", groups = {QpGroup.Del.class})
    private Long id;

    /**
     * 用户名
     */
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确", groups = {QpGroup.Add.class})
    @NotEmpty(message = "手机号码不能为空", groups = {QpGroup.Add.class})
    private String username;

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
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 身份证号
     */
    @Pattern(regexp = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$", message = "身份证格式不正确", groups = {QpGroup.realNameAuth.class})
    @NotEmpty(message = "身份证号不能为空", groups = {QpGroup.Update.class})
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
     * 手握身份证图片
     */
    @NotEmpty(message = "手握身份证图片不能为空", groups = {QpGroup.realNameAuth.class})
    private String realNameImgPerson;

    /**
     * 身份证地址
     */
    @NotEmpty(message = "身份证地址为空", groups = {QpGroup.realNameAuth.class})
    private String address;

    /**
     * 身份证名族
     */
    @NotEmpty(message = "身份证名族不能为空", groups = {QpGroup.realNameAuth.class})
    private String nation;

    /**
     * 性别
     */
    @NotEmpty(message = "性别不能为空", groups = {QpGroup.realNameAuth.class})
    private String sex;

    /**
     * 身份证姓名
     */
    @NotEmpty(message = "身份证姓名不能为空", groups = {QpGroup.realNameAuth.class})
    private String realName;

    /**
     * 签发机关
     */
    @NotEmpty(message = "签发机关不能为空", groups = {QpGroup.realNameAuth.class})
    private String issueAuthority;

    /**
     * 签发日期
     */
    @NotEmpty(message = "签发日期不能为空", groups = {QpGroup.realNameAuth.class})
    private String issueDate;

    /**
     * 失效日期
     */
    @NotEmpty(message = "失效日期不能为空", groups = {QpGroup.realNameAuth.class})
    private String expiryDate;

    /**
     * 默认0为 自己注册 1为管理员分配 2第三方平台提供的账号
     * 账号注册类型
     */
    private Integer registerType;


    /**
     * 是否开通 1默认开通 第三方平台提供默认不开通
     */
    private Integer isOpen;

    /**
     * 备注 (管理员 添加账号时加入 项目)
     */
    private String remark;

    @Transient
    private String authPhone;

}