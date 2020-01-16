package com.fantacg.redistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname SfRegistry 注册中心
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@EnableEurekaServer
@SpringBootApplication
public class SfRegistry {

    public static void main(String[] args) {
        SpringApplication.run(SfRegistry.class, args);
    }

}
