package com.fantacg.user.controller;

import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.user.RealNameAuthLog;
import com.fantacg.common.utils.Result;
import com.fantacg.user.aspect.Requirespermissions;
import com.fantacg.user.service.RealNameAuthLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname RealNameAuthLogController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/realNameAuthLog")
public class RealNameAuthLogController {

    @Autowired
    private RealNameAuthLogService realNameAuthLogService;
    /**
     * 管理员查询实名认证申请列表
     * @return
     */
    @GetMapping("/page")
    @Requirespermissions("realNameAuthLog:page")
    public Result queryRealNameApplyPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) Integer status) {
        PageResult<RealNameAuthLog> result = this.realNameAuthLogService.queryRealNameApplyPage(page,rows,sortBy,desc, username,status);
        return Result.success(result);
    }

    /**
     * 管理员查询申请实名认证的用户信息
     * @param id
     * @return
     */
    @GetMapping("/getRealNameAuthLogDetail/{id}")
    @Requirespermissions("getRealNameAuthLogDetail")
    public Result getRealNameAuthLogDetail(@PathVariable("id") Long id) {
        return  this.realNameAuthLogService.getRealNameAuthLogDetail(id);
    }

    /**
     * 管理员审核实名认证
     * @return
     */
    @PutMapping("/checkRealNameAuth")
    @Requirespermissions("checkRealNameAuth")
    public Result checkRealNameAuth(@RequestBody RealNameAuthLog realNameAuthLog){
        return this.realNameAuthLogService.checkRealNameAuth(realNameAuthLog);
    }

    /**
     * 获取实名认证记录/状态
     * @return
     */
    @GetMapping("/getMyRealNameAuthLog")
    public Result getMyRealNameAuthLog() {
        return  this.realNameAuthLogService.getMyRealNameAuthLog();
    }
}
