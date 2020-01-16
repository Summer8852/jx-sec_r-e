package com.fantacg.user.controller;

import com.fantacg.common.pojo.user.MemberWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.user.service.MemberWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname MemberWorkerController
 * @Created by Dupengfei 2019/12/18 16:02
 * @Version 2.0
 */
@RestController
@RequestMapping("/memberWorker")
public class MemberWorkerController {

    @Autowired
    MemberWorkerService memberWorkerService;

    /**
     * 查询是否关联账号
     *
     * @return 返回信息
     */
    @PostMapping
    public Result queryMemberWorker() {
        return this.memberWorkerService.queryMemberWorker();
    }

    /**
     * 绑定联账号
     *
     * @return 返回信息
     */
    @PostMapping("/saveMemberWorker")
    public Result addMemberWorker(@RequestBody MemberWorker memberWorker) {
        return this.memberWorkerService.addMemberWorker(memberWorker);
    }

    /**
     * 解除关联账号
     *
     * @return 返回信息
     */
    @PostMapping("/delMemberWorker")
    public Result delMemberWorker(@RequestBody MemberWorker memberWorker) {
        return this.memberWorkerService.delMemberWorker(memberWorker);
    }


}
