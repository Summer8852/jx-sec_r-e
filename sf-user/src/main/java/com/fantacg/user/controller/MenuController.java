package com.fantacg.user.controller;

import com.fantacg.common.pojo.user.Menu;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname MenuController 菜单管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 菜单列表
     */
    @GetMapping("/menuList")
    public Result menuList() {
        return menuService.menuList();
    }


    /**
     * 组装权限树（角色管理）
     *
     * @return
     */
    @GetMapping("/getAllMenuAuth")
    public Result getAllMenuAuth() {
        return this.menuService.getAllMenuAuth();
    }


    /**
     * 获取拥有的权限ids（不包含父级id）
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getMyMenuIds/{roleId}")
    public Result getMyMenuIds(@PathVariable("roleId") Long roleId) {
        return this.menuService.getMyMenuIds(roleId);
    }


    /**
     * 根据用户id查询权限列表
     * 授权方法，注意这个方法不能加权限！
     *
     * @return
     */
    @GetMapping("/doGetAuthorizationInfo")
    public Result doGetAuthorizationInfo() {
        List<Menu> menus = this.menuService.doGetAuthorizationInfo();
        return Result.success(menus);
    }


    /**
     * 菜单管理 查询菜单列表
     */
    @GetMapping("/queryMenuLists")
    public Result queryMenuLists() {
        return this.menuService.queryMenuTypeOne(0L);

    }

    /**
     * 添加菜单
     * *
     * * {
     * *     "parentId": 1,
     * *     "title": "标题",
     * *     "perms": "",
     * *     "type": "",
     * *     "icon": "",
     * *     "orderNum": "",
     * *     "path": ""
     * * }
     */
    @PostMapping("/addMenu")
    public Result addMenu(@RequestBody @Validated(QpGroup.Add.class) Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }

        //添加 菜单
        return this.menuService.addMenu(menu);
    }

    /**
     * 修改目录
     *
     * @param menu
     * @param result
     * @return
     */
    @PutMapping("/updateMenu")
    public Result upateMenu(@RequestBody @Validated(QpGroup.Update.class) Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.menuService.updateMenu(menu);
    }


    /**
     * 删除目录
     *
     * @param id
     * @return
     */
    @GetMapping("/removeMenu/{Id}")
    public Result removeMenu(@PathVariable("Id") Long id) {
        return this.menuService.removeMenu(id);
    }


}
