package com.fantacg.user.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.user.service.BirthPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname BirthPlaceController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/birthPlace")
public class BirthPlaceController {

    @Autowired
    BirthPlaceService birthPlaceService;

    @GetMapping("/{cardNum}")
    public Result queryBirthPlaceById(@PathVariable(value = "cardNum", required = true) String cardNum){
        String name = birthPlaceService.queryBirthPlaceById(cardNum);
        return Result.success(name);
    }


}
