package com.fantacg.common.dto.project;

import lombok.Data;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectVo 项目 id 名称 视图
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class ProjectVo {
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目编号
     */
    private String code;

    /**
     * 心昂名称
     */
    private String name;
}
