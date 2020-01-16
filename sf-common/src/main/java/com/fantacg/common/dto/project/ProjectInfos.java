package com.fantacg.common.dto.project;

import com.fantacg.common.pojo.project.ProjectBuilderLicense;
import com.fantacg.common.pojo.project.ProjectCorpInfo;
import com.fantacg.common.pojo.project.ProjectInfo;
import com.fantacg.common.pojo.project.TeamMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectInfos 添加项目 不需要添加数据库
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfos {

    /**
     * 项目基本信息
     */
    private ProjectInfo projectInfo;


    /**
     * 项目施工许可证
     */
    private ProjectBuilderLicense projectBuilderLicense;


    /**
     * 项目参建单位信息
     */
    private List<ProjectCorpInfo> projectCorpInfos;


    /**
     * 项目参建单位-班组
     */
    private List<TeamMaster> teamMasters;


}
