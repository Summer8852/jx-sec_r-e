package com.fantacg.user.mapper;

import com.fantacg.common.pojo.user.Menu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname MenuMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface MenuMapper extends Mapper<Menu> {
    /**
     * 查询普通管理员菜单列表
     * @param userId
     * @return
     */
    List<Long> queryUserMenuId(Long userId);

    /**
     * 查询用户菜单列表
     * @param memberId
     * @return
     */
    List<Long> queryMemberMenuId(Long memberId);

    /**
     * 查询超级管理员菜单列表
     * @return
     */
    List<Long> queryAllMenuId();

    /**
     * 查询级菜单列表
     * @param parentId
     * @return
     */
    List<Menu> queryListParentId(Long parentId);

    /**
     * 查询用户菜单列表
     * @param map
     * @return
     */
    List<Menu> queryPermsByMemberId(Map<String, Long> map);

    /**
     * 根据权限id 查询菜单
     * @param roleId
     * @return
     */
    List<Menu> queryPermsByRoleId(Long roleId);

    /**
     * 菜单管理查询一级目录
     * @param type
     * @return
     */
    List<Menu> queryMenuTypeOne(Long type);


}
