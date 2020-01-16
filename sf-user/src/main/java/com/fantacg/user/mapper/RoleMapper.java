package com.fantacg.user.mapper;

import com.fantacg.common.pojo.user.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname RoleMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface RoleMapper extends Mapper<Role> {

    /**
     * 查询管理员的角色
     *
     * @param id
     * @return
     */
    List<Role> queryUserRole(Long id);

    /**
     * 查询用户的角色
     *
     * @param id
     * @return
     */
    List<Role> queryMemberRole(Long id);
}
