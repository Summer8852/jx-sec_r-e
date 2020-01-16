package com.fantacg.project;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname SfUploadService 企业-项目-班组服务
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.fantacg.project.mapper")
public class SfProject {
    public static void main(String[] args) {
        SpringApplication.run(SfProject.class, args);
    }

    /**
     * fegin调用添加头信息
     *
     * @return
     */
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder().requestInterceptor(new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //通过RequestContextHolder获取本地请求
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if (requestAttributes == null) {
                    return;
                }
                //获取本地线程绑定的请求对象
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                //给请求模板附加本地线程头部信息，主要是cookie信息
                Enumeration headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String name = (String) headerNames.nextElement();
                    requestTemplate.header(name, request.getHeader(name));
                }
            }
        });
    }
}
