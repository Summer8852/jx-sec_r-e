package com.fantacg.common.pojo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname RealNameAuthLog 认证日志表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_real_name_auth_log")
public class RealNameAuthLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 管理员id
     */
    private Long userId;

    /**
     * 认证状态  0审核中  1同意 2 不同意
     */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 失败信息
     */
    private String msg;

    @Transient
    private String username;

    /**
     * 认证类型
     */
    private Integer type;
}
