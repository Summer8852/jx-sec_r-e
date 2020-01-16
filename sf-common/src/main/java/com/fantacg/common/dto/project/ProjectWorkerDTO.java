package com.fantacg.common.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectWorkerDTO
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWorkerDTO {

//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    /**
     * 工人姓名/身份证
     */
    private String key;
    /**
     * 工人姓名
     */
    private String workerName;
    /**
     * 证件号码
     */
    private String idCardNumber;
    /**
     * 企业ID
     */
    private String corpCode;
    /**
     * 项目ID
     */
    private String projectCode;
    /**
     * 班组ID
     */
    private String teamSysNo;
    /**
     * 工种
     */
    private String workType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private Long memberId;

    /**
     * 分页使用
     */
    private Integer page;
    /**
     * 分页使用
     */
    private Integer rows;

}
