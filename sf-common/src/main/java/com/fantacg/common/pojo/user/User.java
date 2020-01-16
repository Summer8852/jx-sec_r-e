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
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname User 管理员表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "ID不能为空", groups = {QpGroup.Update.class, QpGroup.Del.class})
    private Long id;

    /**
     * 用户名
     */
    @Length(min = 1, max = 30, message = "用户名只能在4~30位之间")
    @NotEmpty(message = "用户名不能为空", groups = {QpGroup.Add.class})
    private String username;

    /**
     * 密码
     */
    @Length(min = 1, max = 30, message = "密码只能在4~30位之间")
    @NotEmpty(message = "密码不能为空", groups = {QpGroup.Add.class})
    private String password;

    /**
     * 电话
     */
    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    @NotEmpty(message = "电话不能为空", groups = {QpGroup.Add.class})
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码的盐值
     */
    @JsonIgnore
    private String salt;

    @Transient
    private List<Map<String, String>> roles;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
