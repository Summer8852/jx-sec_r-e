package com.fantacg.user.config;

import com.fantacg.user.filter.LoginInterceptor;
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
    JwtProperties props;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置登录拦截器
        registry.addInterceptor(new LoginInterceptor(props))
                .addPathPatterns("/**")
                .excludePathPatterns("/query/**")
                // 注册
                .excludePathPatterns("/register")
                // 用户名和密验证码登录
                .excludePathPatterns("/member/loginByPhone")
                // 用户名和密码登录
                .excludePathPatterns("/member/loginMember")

                .excludePathPatterns("/check/**")
                .excludePathPatterns("/send/register/code/**")
                .excludePathPatterns("/send/login/code/**")
                //用户注册+认证
                .excludePathPatterns("/member/auth/register/**")
                //用户名校验
                .excludePathPatterns("/member/check/**")
                //发送邮箱
                .excludePathPatterns("/send/email")
                //管理员登陆
                .excludePathPatterns("/loginUser")
                //邀请人根据uuid 查询需要认证的信息
                .excludePathPatterns("/member/uid/**")
                //邀请人确认认证
                .excludePathPatterns("/member/auth/register/**")
                //忘记密码发送验证码
                .excludePathPatterns("/send/forgetCode/**")
                //忘记密码 修改密码
                .excludePathPatterns("/member/forgetPassword")
                //工人注册获取验证码
                .excludePathPatterns("/send/wrc/**")
                //工人登录获取验证码
                .excludePathPatterns("/send/wlc/**")
                //工人注册
                .excludePathPatterns("/worker")
                // 工人登录
                .excludePathPatterns("/worker/wl/**");
         }
}
