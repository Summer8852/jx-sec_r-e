package com.fantacg.common.pojo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AuthLog 申请认证日志表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_auth_log")
public class AuthLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 申请人id
     */
    private Long memberId;

    /**
     * 认证人id
     */
    private Long authMemberId;
    /**
     * 认证人类型
     */
    private Integer type;

    /**
     * 认证时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date authTime;

    /**
     * 企业id
     */
    private Long corpBasicInfoId;

    /**
     * 申请时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyForTime;

    /**
     * 认证状态 0认证中 1认证成功 2 认证失败
     */
    private Integer status;

}
