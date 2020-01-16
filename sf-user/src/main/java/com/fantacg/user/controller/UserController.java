package com.fantacg.user.controller;

import cn.hutool.core.lang.Console;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.pojo.user.Role;
import com.fantacg.common.pojo.user.User;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.aspect.Requirespermissions;
import com.fantacg.user.service.RoleService;
import com.fantacg.user.service.UserService;
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
 * @Classname UserController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 查询所有用户
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("/page")
    @Requirespermissions("user:page")
    public Result queryUserByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        return this.userService.queryUserByPageAndSort(page, rows, sortBy, desc, key);
    }


    /**
     * 管理员登录
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/loginUser")
    public  Result loginUser(@RequestBody MemberDto memberDto) {
        return this.userService.loginUser(memberDto);
    }


    /**
     * 根据用户Id查询用户角色
     */
    @GetMapping("/queryUserRole/{id}")
    @Requirespermissions("user:page")
    public Result queryUserRole(@PathVariable("id") Long id) {
        if (id == null || id < 0) {
            // id为null或者小于等于0，响应400
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        List<Role> list = this.roleService.queryUserRole(id);
        return Result.success(list);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/queryAllUserRole")
    @Requirespermissions("user:page")
    public Result queryAllUserRole() {
        List<Role> list = this.roleService.queryAllUserRole();
        return Result.success(list);
    }

    /**
     * 根据id删除用户
     */
    @DeleteMapping("/delete/{id}")
    @Requirespermissions("user:del")
    public Result delete(@PathVariable(value = "id") Long id) {
        if (StringUtils.isEmpty(id)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.userService.delete(id);

    }


    /**
     * 根据Id查询用户
     */
    @GetMapping("/queryUserById/{id}")
    @Requirespermissions("user:queryById")
    public Result queryUserById(@PathVariable(value = "id") Long id) {
        if (id == null || id == 0) {
            // id为null或者小于等于0，响应400
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.userService.queryUserById(id);

    }


    /**
     * 新增管理员信息
     */
    @PostMapping
    @Requirespermissions("user:add")
    public Result add(@RequestBody @Validated(QpGroup.Add.class) User user, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.userService.addUser(user);


    }

    /**
     * 修改用户信息
     */
    @PutMapping
    @Requirespermissions("user:edit")
    public Result editUser(@RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.userService.editUser(user);
    }

}
