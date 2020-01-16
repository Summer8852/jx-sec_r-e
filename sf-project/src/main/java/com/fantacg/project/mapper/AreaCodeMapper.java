package com.fantacg.project.mapper;

import com.fantacg.common.pojo.project.AreaCode;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AreaCodeMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface AreaCodeMapper extends Mapper<AreaCode> {

    /**
     * 根据 code 查询城市编码 和名称
     *
     * @param code 城市编码
     * @return 城市编码 和名称
     */
    Map<String, Object> selectAreaCodeName(String code);

    /**
     * 查询城市父级编码及名称
     *
     * @return 城市父级编码及名称
     */
    List<AreaCode> selectParentAreaCodeName();

    /**
     * 查询城市父级编码及名称
     *
     * @param parentCode
     * @return 城市父级编码及名称
     */
    List<AreaCode> selectAreaCodeNameList(String parentCode);
}
