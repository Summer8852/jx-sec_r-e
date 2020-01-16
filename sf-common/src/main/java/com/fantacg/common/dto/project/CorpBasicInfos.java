package com.fantacg.common.dto.project;


import com.fantacg.common.pojo.project.CorpBasicInfo;
import com.fantacg.common.pojo.project.CorpCertInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpBasicInfos 管理员添加企业 不需要添加数据库
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorpBasicInfos {

    /**
     * 企业基本信息
     */
    private CorpBasicInfo corpBasicInfo;

    /**
     * 企业资质数据表
     */
    private List<CorpCertInfo> corpCertInfos;

}
