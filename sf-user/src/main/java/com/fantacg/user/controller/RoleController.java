package com.fantacg.user.controller;

import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.user.Role;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.aspect.Requirespermissions;
import com.fantacg.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname RoleController 角色管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 查询所有角色(分页查询)
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("/page")
    @Requirespermissions("role:page")
    public Result queryRoleByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        PageResult<Role> result = this.roleService.queryRoleByPageAndSort(page, rows, sortBy, desc, key);
        return Result.success(result);
    }

    /**
     * 修改角色
     *
     * @return
     */
    @PutMapping
    @Requirespermissions("role:edit")
    public Result edit(@RequestBody @Validated(QpGroup.Update.class) Role role, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.roleService.edit(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @Requirespermissions("role:add")
    public Result save(@RequestBody @Valid Role role) {
        return this.roleService.save(role);
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Requirespermissions("role:del")
    public Result del(@PathVariable(value = "id") Long id) {
        if (StringUtils.isEmpty(id)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.roleService.del(id);
    }
}
