package com.fantacg.video;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 视频服务
 * @author DUPENGFEI
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients
@MapperScan("com.fantacg.video.mapper")
public class SfVideo {

    public static void main(String[] args) {
        SpringApplication.run(SfVideo.class, args);
    }

    /**
     * fegin调用添加头信息
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
