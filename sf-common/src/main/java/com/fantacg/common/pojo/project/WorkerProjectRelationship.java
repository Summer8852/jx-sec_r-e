package com.fantacg.common.pojo.project;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 工人项目关联表
 * @author DUPENGFEI
 */

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname 工人项目关联表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name="pb_worker_project_relationship")
public class WorkerProjectRelationship implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工人id
     */
    private Long workerId;

    /**
     * 工人加入项目的日期
     */
    private Date timeStart;

    /**
     * 工人离开项目的日期
     */
    private Date timeEnd;

    /**
     * 项目的编号
     */
    private String projectCode;

    /**
     * 审批状态
     */
    private String status;

    /**
     * 审批时间
     */
    private Date applyTime;
}
