package com.fantacg.user.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.entity.MemberInfo;
import com.fantacg.common.auth.entity.UserInfo;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.Menu;
import com.fantacg.common.pojo.user.RoleMenu;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.MenuMapper;
import com.fantacg.user.mapper.RoleMenuMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname MenuService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询菜单列表
     *
     * @return
     */
    public Result menuList() {
        try {
            Claims claims = LoginInterceptor.getLoginClaims();
            List<Menu> list = new ArrayList<Menu>();
            if (claims != null) {
                //如果是管理员
                if (RoleConstant.USER.equals(claims.get("roles"))) {
                    UserInfo user = new UserInfo(ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID)), ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_USER_NAME)));
                    //超级管理员
                    if (user.getId() == 1) {
                        //查询所有菜单 获取用户的所有权限-菜单(显示的)
                        List<Long> menuIdList = menuMapper.queryAllMenuId();
                        list = getAllMenuList(menuIdList);
                    } else {
                        String redisKey = KeyConstant.MENU_LIST_KEY + "roles";
                        String str = redisTemplate.opsForValue().get(redisKey);
                        if (StringUtil.isNotEmpty(str)) {
                            list = objectMapper.readValue(str, List.class);
                            return Result.success(list);
                        }
                        //普通管理员，权限-菜单
                        List<Long> menuIdList = menuMapper.queryUserMenuId(user.getId());
                        list = getAllMenuList(menuIdList);
                        if (!list.isEmpty()) {
                            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(list));
                        }
                    }
                } else {
                    String redisKey = KeyConstant.MENU_LIST_KEY + RoleConstant.MEMBER;
                    String str = redisTemplate.opsForValue().get(redisKey);
                    if (StringUtil.isNotEmpty(str)) {
                        list = objectMapper.readValue(str, List.class);
                        return Result.success(list);
                    }
                    //如果是用户
                    MemberInfo member = new MemberInfo(
                            ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID)),
                            ObjectUtils.toString(claims.get(JwtConstans.JWT_KEY_USER_NAME)));
                    //获取用户的所有权限-菜单
                    List<Long> menuIdList = menuMapper.queryMemberMenuId(member.getId());
                    list = getAllMenuList(menuIdList);
                    if (!list.isEmpty()) {
                        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(list));
                    }
                }
                if (!list.isEmpty()) {
                    return Result.success();
                }
                return Result.success(list);
            } else {
                return Result.success();
            }
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 根据用户id查询用户权限列表
     *
     * @return
     */
    public List<Menu> doGetAuthorizationInfo() {
        Long id = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);
        //从顶级菜单开始查
        map.put("parentId", 0L);
        //先查所有目录
        map.put("type", 0L);
        //一级菜单
        List<Menu> list = this.menuMapper.queryPermsByMemberId(map);
        //根据每一个一级菜单查下面的所有二级菜单
        for (Menu menu : list) {
            Map<String, Long> map2 = new HashMap<>();
            map2.put("id", id);
            //type=1 查菜单列表
            map2.put("parentId", menu.getId());
            map2.put("type", 1L);
            //二级菜单
            List<Menu> l = this.menuMapper.queryPermsByMemberId(map2);
            menu.setItems(l);
            for (Menu m : l) {
                Map<String, Long> map3 = new HashMap<>();
                map3.put("id", id);
                map3.put("parentId", m.getId());
                //2查权限列表
                map3.put("type", 2L);
                List<Menu> lm = this.menuMapper.queryPermsByMemberId(map3);
                m.setItems(lm);
            }
        }
        return list;
    }

    /**
     * 获取所有菜单列表
     */
    private List<Menu> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<Menu> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 查询根菜单列表
     *
     * @param parentId
     * @param menuIdList
     * @return
     */
    private List<Menu> queryListParentId(Long parentId, List<Long> menuIdList) {
        //查询下级菜单列表
        List<Menu> menuList = menuMapper.queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<Menu> userMenuList = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menuIdList.contains(menu.getId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }


    /**
     * 递归
     */
    private List<Menu> getMenuTreeList(List<Menu> menuList, List<Long> menuIdList) {
        List<Menu> subMenuList = new ArrayList<>();
        for (Menu entity : menuList) {
            //目录
            if (entity.getType() == 0L) {
                entity.setAction(entity.getIcon());
                entity.setItems(getMenuTreeList(queryListParentId(entity.getId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }


    public Result getAllMenuAuth() {
        //这一部分为获取所有的权限树结构
        Map<String, Long> map = new HashMap<>();
        map.put("id", null);
        //从顶级菜单开始查
        map.put("parentId", 0L);
        //先查所有的目录
        map.put("type", 0L);
        //一级菜单
        List<Menu> list = this.menuMapper.queryPermsByMemberId(map);
        //根据每一个一级菜单查下面的所有二级菜单
        for (Menu menu : list) {
            Map<String, Long> map2 = new HashMap<>();
            map2.put("id", null);
            map2.put("parentId", menu.getId());
            //type=1 查菜单列表
            map2.put("type", 1L);
            //二级菜单
            List<Menu> l = this.menuMapper.queryPermsByMemberId(map2);

            menu.setLabel(menu.getTitle());
            menu.setChildren(l);

            //遍历所有二级菜单，返回权限列表
            for (Menu m : l) {
                Map<String, Long> map3 = new HashMap<>();
                map3.put("id", null);
                map3.put("parentId", m.getId());
                //2查权限列表
                map3.put("type", 2L);
                List<Menu> lm = this.menuMapper.queryPermsByMemberId(map3);
                m.setLabel(m.getTitle());
                m.setChildren(lm);
                for (Menu mm : lm) {
                    mm.setLabel(mm.getTitle());
                }
            }
        }
        return Result.success(list);
    }

    public Result getMyMenuIds(Long roleId) {
        //下面这一部分为获取用户拥有的权限(父级权限不打钩)，组装打钩数据
        List<Long> menuIds = new ArrayList<>();
        List<Menu> listMenu = this.menuMapper.queryPermsByRoleId(roleId);
        for (Menu m : listMenu) {
            menuIds.add(m.getId());
        }
        for (Menu m : listMenu) {
            if (menuIds.contains(m.getParentId())) {
                menuIds.remove(m.getParentId());
            }
        }
        //将要勾选的权限放入list的第一个Menu里
        return Result.success(menuIds);
    }

    /**
     * 菜单管理查询一级目录
     *
     * @param type
     * @return
     */
    public Result queryMenuTypeOne(Long type) {
        List<Menu> menus = new ArrayList<>();
        try {
            //获取redis是否有数据
            String s = redisTemplate.opsForValue().get(KeyConstant.MENU_LIST_KEY);
            if (!StringUtils.isEmpty(s)) {
                menus = objectMapper.readValue(s, List.class);
                return Result.success(menus);
            }
            //redis 没有数据查询数据库
            menus = this.menuMapper.queryMenuTypeOne(type);
            for (Menu menu : menus) {
                List<Menu> menus1 = this.menuMapper.queryListParentId(menu.getId());
                menu.setChildren(menus1);
                for (Menu menu1 : menus1) {
                    List<Menu> menus2 = this.menuMapper.queryListParentId(menu1.getId());
                    menu1.setChildren(menus2);
                }
            }
            if (!menus.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.MENU_LIST_KEY, objectMapper.writeValueAsString(menus));
            }

            return Result.success(menus);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 添加菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addMenu(Menu menu) {
        //添加  0：目录  或 1：菜单 时, 必须传path 不能为空
        if (menu.getType() == 0 || menu.getType() == 1) {
            if (org.springframework.util.StringUtils.isEmpty(menu.getPath())) {
                return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
            }
        }

        //删除换存
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY);
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + "roles");
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + RoleConstant.MEMBER);

        Menu m = new Menu();
        m.setParentId(menu.getParentId());
        m.setTitle(menu.getTitle());
        m.setType(menu.getType());
        int counts = this.menuMapper.selectCount(m);
        //查询是否重复
        if (counts == 0) {
            int i = this.menuMapper.insertSelective(menu);
            if (i > 0) {
                redisTemplate.delete(KeyConstant.MENU_LIST_KEY);
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            }
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    public Result updateMenu(Menu menu) {
        //删除换存
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY);
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + "roles");
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + RoleConstant.MEMBER);
        int i = this.menuMapper.updateByPrimaryKey(menu);
        if (i > 0) {
            redisTemplate.delete(KeyConstant.MENU_LIST_KEY);
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 删除菜单
     */
    public Result removeMenu(Long id) {
        //删除换存
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY);
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + "roles");
        redisTemplate.delete(KeyConstant.MENU_LIST_KEY + RoleConstant.MEMBER);
        // 删除父级目录
        int i = this.menuMapper.deleteByPrimaryKey(id);
        // 删除子级目录
        Example example = new Example(RoleMenu.class);
        example.createCriteria().andCondition(" menu_id = " + id);
        this.roleMenuMapper.deleteByExample(example);
        if (i > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

}
