package com.fantacg.answer.controller;

import com.fantacg.answer.service.QuickAnswerService;
import com.fantacg.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname QuickAnswerController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/quickAnswer")
public class QuickAnswerController {

    @Autowired
    QuickAnswerService quickAnswerService;

    /**
     * 查询全部快速答题题目
     */
    @GetMapping
    public Result queryQuickAnswerAll(){
        return this.quickAnswerService.queryQuickAnswerAll();
    }

    /**
     * 随机出题
     */
    @GetMapping("/random/{num}")
    public Result queryQuickAnswerRandom(@PathVariable("num") Integer num){
        return this.quickAnswerService.queryQuickAnswerRandom(num);
    }



}
