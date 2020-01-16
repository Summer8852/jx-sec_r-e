package com.fantacg.common.pojo.user;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * 邮箱服务
 *
 * @author DUPENGFEI
 */

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname SmtpMail 邮箱服务
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_smtp_mail")
public class SmtpMail implements Serializable {

    private Long id;

    /**
     *SMTP服务器
     */
    private String smtpHost;

    /**
     * 发件人昵称
     */
    private String smtpName;

    /**
     * 发件人邮箱
     */
    private String smtpMail;

    /**
     * 邮箱登入密码
     */
    private String password;

    /**
     * SMTP端口号
     */
    private String port;

    /**
     * 协议
     */
    private String protocol;
}

