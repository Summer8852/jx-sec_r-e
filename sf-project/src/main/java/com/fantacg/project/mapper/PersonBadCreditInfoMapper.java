package com.fantacg.project.mapper;

import com.fantacg.common.pojo.project.PersonBadCreditInfo;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

/**
 * 不良行为记录
 * @author User
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P> 不良行为记录
 * @author 智慧安全云
 * @Classname PersonBadCreditInfoMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface PersonBadCreditInfoMapper extends Mapper<PersonBadCreditInfo> {

    /**
     * 分页查询不良行为记录
     * @param params
     * @return
     */
    Page<PersonBadCreditInfo> queryPersonBadCreditByPage(Map<String, Object> params);
}
