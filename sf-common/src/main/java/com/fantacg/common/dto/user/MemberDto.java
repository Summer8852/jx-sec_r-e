package com.fantacg.common.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname MemberDto 注册时使用  不需要添加数据库
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    /**
     * 注册类型
     */
    private String registerType;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 账号
     */
    private String account;

    /**
     * 验证码
     */
    private String code;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码 （验证码注册初始化密码 默认 6个0 待确认）
     */
    private String password;

    /**
     * 邀请人手机号
     */
    private String authPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;


}
