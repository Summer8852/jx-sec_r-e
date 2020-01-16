package com.fantacg.project.mapper;

import com.fantacg.common.pojo.project.PersonGoodCreditInfo;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PersonGoodCreditInfoMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface PersonGoodCreditInfoMapper extends Mapper<PersonGoodCreditInfo> {

    /**
     * 分页查询良好行为记录
     * @param params
     * @return
     */
    Page<PersonGoodCreditInfo> selectAllPersonGoodCreditInfoByPage(Map<String, Object> params);

}
