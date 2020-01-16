package com.fantacg.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname GlobalCorsConfig
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {

        //添加映射路径，我们拦截一切请求
        final UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        //1.添加CORS配置信息
        final CorsConfiguration config = new CorsConfiguration();

        //2) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("http://sf.local.com");
        config.addAllowedOrigin("http://sf.fantacg.com");
        config.addAllowedOrigin("https://sf.fantacg.com");
        //允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        //是否发送Cookie信息 允许cookies跨域
        config.setAllowCredentials(true);
        //预检请求的缓存时间（秒 推荐18000L），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        //允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        configSource.registerCorsConfiguration("/**", config);
        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
