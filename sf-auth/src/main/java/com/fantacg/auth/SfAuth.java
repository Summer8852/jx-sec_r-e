package com.fantacg.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname SfAuthService 权限控制服务
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class SfAuth {

    public static void main(String[] args) {
        SpringApplication.run(SfAuth.class, args);
    }

}
