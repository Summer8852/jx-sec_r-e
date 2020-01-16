package com.fantacg.user.controller;


import com.fantacg.common.pojo.worker.WorkerInfo;
import com.fantacg.common.utils.Result;
import com.fantacg.user.service.WorkerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname WorkerInfoController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController("/workerInfo")
public class WorkerInfoController {

    @Autowired
    WorkerInfoService workerInfoService;

    /**
     * 身份证号查询信息 AES加密身份证号
     *
     * @param workerInfo
     * @return
     */
    @PostMapping("/w-card")
    public Result queryWorkerInfoByCardNum(@RequestBody WorkerInfo workerInfo) {
        return this.workerInfoService.queryWorkerInfoByCardNum(workerInfo);
    }

    /**
     * 管理员分页查询实人信息
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping
    public Result queryProjectTrainingByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc) {
        return this.workerInfoService.queryWorkerByPage(page, rows, sortBy, desc);
    }

}
