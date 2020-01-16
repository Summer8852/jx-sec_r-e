package com.fantacg.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname SfGateway 网关服务
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class SfGateway extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SfGateway.class, args);
    }

    /**
     * 为了打包springboot项目
     *
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}