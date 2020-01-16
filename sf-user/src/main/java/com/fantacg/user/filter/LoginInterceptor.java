package com.fantacg.user.filter;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtUtils;
import com.fantacg.common.utils.CookieUtils;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.config.JwtProperties;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname LoginInterceptor 拦截器
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtProperties props;

    //ThreadLocal 维护变量 避免同步
    //ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
    // 开始时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义一个线程域，存放登录的对象
     */
    private static final ThreadLocal<Claims> T_1 = new ThreadLocal<>();

    public LoginInterceptor() {
        super();
    }

    public LoginInterceptor(JwtProperties props) {
        this.props = props;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        //查询Token
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        if (StringUtils.isBlank(token)) {
            //用户未登录,返回401，拦截
            response.getWriter().append(JSON.toJSONString(Result.failure(ResultCode.LOGIN_TOKEN_EXPIRE_ERROR)));
            return false;
        }
        //用户已登录，获取用户信息
        try {
            Claims claims = new JwtUtils().parseJWT(props.getPublicKey(), token);
            //放入线程域中
            T_1.set(claims);
            return true;
        } catch (Exception e) {
            //抛出异常，未登录
            response.getWriter().append(JSON.toJSONString(Result.failure(ResultCode.LOGIN_TOKEN_EXPIRE_ERROR)));
            return false;
        }
    }

//    private void appendAllPermission(StringBuffer sb, Menu m) {
//        if(m.getItems()!=null){
//            for(Menu menu:m.getItems()){
//                sb.append(menu.getPerms()+",");
//                appendAllPermission(sb,menu);
//            }
//        }
//    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //过滤器完成后，从线程域中删除用户信息
        T_1.remove();
    }

    /**
     * 获取登陆用户
     *
     * @return
     */
    public static Claims getLoginClaims() {
        return T_1.get();
    }
}
