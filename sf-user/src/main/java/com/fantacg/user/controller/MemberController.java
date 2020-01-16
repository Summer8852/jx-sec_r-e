package com.fantacg.user.controller;

import com.fantacg.user.aspect.Requirespermissions;
import com.fantacg.user.service.MemberService;
import com.fantacg.user.service.RoleService;
import com.fantacg.common.dto.user.MemberDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.Member;
import com.fantacg.common.utils.QpGroup;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname MemberController 用户管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    RoleService roleService;

    /**
     * 注册
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/register")
    public Result registerMember(@RequestBody MemberDto memberDto) {
        try {
            return this.memberService.registerMember(memberDto);
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 根据用户名和密验证码登录
     *
     * @param memberDto
     * @return
     */
    @PostMapping("/loginByPhone")
    public Result loginMemberByPhone(@RequestBody MemberDto memberDto) {
        try {
            return this.memberService.queryMemberByUserName(memberDto);
        } catch (Exception e) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 根据用户名和密码登录
     *
     * @return
     */
    @PostMapping("/loginMember")
    public Result loginMember(@RequestBody MemberDto memberDto) {
        return this.memberService.loginMember(memberDto);
    }

    /**
     * 校验用户名是否可用
     *
     * @return 成功/失败
     */
    @GetMapping("/check/{username}")
    public Result checkUserData(@PathVariable(value = "username") String username) throws Exception {
        return this.memberService.checkData(username);
    }

    /**
     * uuid查询邀请人信息
     */
    @GetMapping("/uid/{uuid}")
    public Result queryRegisterUrl(@PathVariable("uuid") String uuid) {
        try {
            return this.memberService.queryRegisterUrl(uuid);
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 邀请注册认证
     *
     * @param uuid uuid
     * @return 成功/失败
     */
    @GetMapping("/auth/register/{uuid}")
    public Result register(@PathVariable("uuid") String uuid) {
        try {
            return this.memberService.updateAuthStatus(uuid);
        } catch (Exception e) {
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 用户提交实名认证申请
     *
     * @param member 用户信息
     * @return 申请 成功 /失败
     */
    @PutMapping("/applyForRealNameAuth")
    public Result applyForRealNameAuth(@RequestBody @Validated(QpGroup.realNameAuth.class) Member member, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        return this.memberService.applyForRealNameAuth(member);
    }

    /**
     * 获取企业认证状态updateAuthStatus
     *
     * @return
     */
    @GetMapping("/getMemeberAuthInfo")
    public Result getMemeberAuthInfo() {
        return this.memberService.getMemeberAuthInfo();
    }

    /**
     * 查询所有用户
     *
     * @param page   分页
     * @param rows   分页
     * @param sortBy 搜索
     * @param desc   排序
     * @param key    key
     * @return 用户列表
     */
    @GetMapping("/page")
    @Requirespermissions("member:page")
    public Result queryMemberByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        return this.memberService.queryMemberByPageAndSort(page, rows, sortBy, desc, key);
    }


    /**
     * 管理员查询-申请企业认证列表
     *
     * @return 申请成功/失败
     */
    @GetMapping("/getApplyForAuthByPage")
    @Requirespermissions("member:applyForAuthPage")
    public Result getApplyForAuthByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                        @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                        @RequestParam(value = "key", required = false) String key) {
        return this.memberService.getApplyForAuthByPage(page, rows, sortBy, desc, key);
    }


    /**
     * 根据id删除用户
     *
     * @param id 用户id
     * @return 删除成功/失败
     */
    @DeleteMapping("/delete/{id}")
    @Requirespermissions("member:del")
    public Result delete(@PathVariable(value = "id") Long id) {
        if (StringUtils.isEmpty(id)) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.memberService.delete(id);

    }


    /**
     * 根据用户Id查询用户角色
     *
     * @param id 用户Id
     * @return 返回用户角色信息
     */
    @GetMapping("/queryMemberRole/{id}")
    @Requirespermissions("member:page")
    public Result queryMemberRole(@PathVariable("id") Long id) {
        return this.roleService.queryMemberRole(id);
    }

    /**
     * 管理员修改用户信息
     *
     * @param member  用户信息
     * @param roleIds 角色信息
     * @return 修改成功/失败
     */
    @PutMapping
    @Requirespermissions("member:edit")
    public Result editUser(Member member, @RequestParam("roleIds") List<Long> roleIds) {
        return this.memberService.editUser(member, roleIds);
    }


    /**
     * 忘记密码
     */
    @PutMapping("/forgetPassword")
    public Result forgetPassword(@RequestBody MemberDto memberDto) {
        return memberService.forgetPassword(memberDto);
    }


    /**
     * 用户/管理员修改自己的密码
     *
     * @param map 用户/管理员信息
     * @return 修改成功/失败
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestParam Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("newPassword"))) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        return this.memberService.updatePassword(map);
    }


    /**
     * 查询用户账号绑定状态
     *
     * @return 返回用户信息
     */
    @GetMapping("/accountInfo")
    public Result getMyAccountInfo() {
        return this.memberService.getMyAccountInfo();
    }

    /**
     * 绑定手机/邮箱
     *
     * @param type  类型
     * @param param 邮箱
     * @param code  验证码
     * @return 绑定是否成功
     */
    @PutMapping("/bind/{type}/{param}/{code}")
    public Result bind(@PathVariable("type") String type, @PathVariable("param") String param, @PathVariable("code") String code) {
        return memberService.bind(type, param, code);
    }


    /**
     * 管理员添加用户账号
     *
     * @param memberDto 账号信息
     * @return 添加成功/失败
     */
    @PostMapping("/registerMember")
    @Requirespermissions("user:registermember")
    public Result adminRegisterMember(@RequestBody MemberDto memberDto) {
        return this.memberService.adminRegisterMember(memberDto);
    }


    /**
     * 初始化密码
     *
     * @param member 用户信息
     * @return 初始化成功/失败
     */
    @PostMapping("/initializationPwd")
    @Requirespermissions("user:initializationPwd")
    public Result initializationPwd(@RequestBody Member member) {
        return this.memberService.initializationPwd(member);
    }


    /**
     * 确认开通
     *
     * @param member 用户信息
     * @return 开通成功/失败
     */
    @PostMapping("/isOpen")
    @Requirespermissions("user:isOpen")
    public Result isOpen(@RequestBody Member member) {
        return this.memberService.isOpen(member);
    }


}



