package com.fantacg.project.mapper;

import com.fantacg.common.pojo.project.CorpBasicInfo;
import com.fantacg.common.pojo.project.CorpCertInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpBasicInfoMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface CorpBasicInfoMapper extends Mapper<CorpBasicInfo> {

    int installCorpBasicInfo(CorpBasicInfo corpBasicInfo);

    String selectCorpBasicInfoCode(Long memberId, String corpCode);

    String selectCorpBasicInfoName(Long memberId, String corpName);

    List<CorpBasicInfo> queryCorpBasicInfoListPage(Map<String, Object> params);

    List<Map<String, Object>> queryCorpBasicInfoCorpName(HashMap<String, Object> map);

    CorpBasicInfo queryCorpBasicInfoById(Long id, Long memberId);

    int updateCorpBasicInfo(CorpBasicInfo corpBasicInfo);

    Long updateCorpBasicInfoByCode(CorpBasicInfo corpBasicInfo);

    List<Map<String, Object>> searchCorpBasicInfo(HashMap<String, Object> params);

    CorpCertInfo queryCorpCertInfoById(Long id);


}
