package com.fantacg.project.client;

import com.fantacg.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname UserClient 远程调用 user-service
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@FeignClient(value = "user-service")
public interface UserClient {

    /**
     * 根据用户id查询权限列表
     *
     * @return
     */
    @GetMapping("/menu/doGetAuthorizationInfo")
    Result doGetAuthorizationInfo();

    /**
     * 获取用户认证信息
     *
     * @return
     */
    @GetMapping("/member" + "/getMemeberAuthInfo")
    Result getMemeberAuthInfo();
}