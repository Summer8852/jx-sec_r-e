package com.fantacg.project.config;

import com.fantacg.project.filter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
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
                //查询学历列表
                .excludePathPatterns("/sysDict/getSelectParams/**")
                //根据工人身份证查询工人信息
                .excludePathPatterns("/worker/getWorkerByIdcardNumber/**")
                //工人信息录入
                .excludePathPatterns("/worker")
                //查询设备信息
                .excludePathPatterns("/equipment/queryPlatformKeyByKey")
                //扫描工人身份证
                .excludePathPatterns("/worker/scanIdCard")
                //安全教育培训箱上报数据接口
                .excludePathPatterns("/receive/**")
                //数据拉取
                .excludePathPatterns("/settingData/pull");
    }
}
