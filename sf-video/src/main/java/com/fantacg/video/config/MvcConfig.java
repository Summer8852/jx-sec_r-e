package com.fantacg.video.config;

import com.fantacg.video.filter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname MvcConfig
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties props;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置登录拦截器
        registry.addInterceptor(new LoginInterceptor(props)).addPathPatterns("/**")
                .excludePathPatterns("/getVideoByVideoId**")
                .excludePathPatterns("/index")
                .excludePathPatterns("/rand/**")
                .excludePathPatterns("/detail/**")
                .excludePathPatterns("/category/list")
                .excludePathPatterns("/test")
                .excludePathPatterns("/random/*");
    }
}
