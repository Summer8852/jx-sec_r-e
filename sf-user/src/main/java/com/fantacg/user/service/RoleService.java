package com.fantacg.user.service;

import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.user.Role;
import com.fantacg.common.pojo.user.RoleMenu;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.mapper.RoleMapper;
import com.fantacg.user.mapper.RoleMenuMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname RoleService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 查询所有角色
     *
     * @return
     */
    public List<Role> queryAllUserRole() {
        return roleMapper.selectAll();
    }

    /**
     * 查询管理员的角色
     *
     * @param id
     * @return
     */
    public List<Role> queryUserRole(Long id) {
        return roleMapper.queryUserRole(id);
    }

    /**
     * 查询用户的角色
     *
     * @param id
     * @return
     */
    public Result queryMemberRole(Long id) {
        List<Role> roles = this.roleMapper.queryMemberRole(id);
        return Result.success(roles);
    }

    public PageResult<Role> queryRoleByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Role.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Role> pageInfo = (Page<Role>) roleMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    /**
     * 修改角色
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result edit(Role role) {
        //删除换存
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + RoleConstant.MEMBER);
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + RoleConstant.ROLES);

        //修改角色权限
        Example example = new Example(RoleMenu.class);
        example.createCriteria().andCondition(" role_id = " + role.getId());
        roleMenuMapper.deleteByExample(example);

        for (Long menuId : role.getChecked()) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insertSelective(roleMenu);
        }

        //修改角色信息
        int i = roleMapper.updateByPrimaryKeySelective(role);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);

    }

    /**
     * 新增角色
     */
    @Transactional(rollbackFor = Exception.class)
    public Result save(Role role) {
        role.setCreateTime(new Date());
        roleMapper.insertSelective(role);

        for (Long menuId : role.getChecked()) {
            RoleMenu roleMenu;
            roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insertSelective(roleMenu);
        }
        return Result.success(ResultCode.DATA_ADD_SUCCESS);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result del(Long id) {
        Role role = new Role();
        role.setId(id);
        //修改角色权限
        Example example = new Example(RoleMenu.class);
        example.createCriteria().andCondition(" role_id = " + role.getId());
        roleMenuMapper.deleteByExample(example);
        roleMapper.deleteByPrimaryKey(role);
        return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
    }


}
