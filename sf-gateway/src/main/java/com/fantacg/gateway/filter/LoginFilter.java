package com.fantacg.gateway.filter;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtUtils;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.*;
import com.fantacg.gateway.config.FilterProperties;
import com.fantacg.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname LoginFilter
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProp;

    @Autowired
    private FilterProperties filterProp;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest req = ctx.getRequest();
        // 获取路径
        String requestURI = req.getRequestURI();
        // 判断白名单
        return !isAllowPath(requestURI);
    }

    private boolean isAllowPath(String requestURI) {
        // 定义一个标记
        boolean flag = false;
        // 遍历允许访问的路径
        for (String path : this.filterProp.getAllowPaths()) {
            // 然后判断是否是符合
            if (requestURI.startsWith(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * token 校验
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {

        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 判断是否传入Cookie
        if (request.getCookies() == null) {
            return null;
        }
        // 获取token
        String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        // 退出登录 清空服务器Cookie 前端页面未清除 请求解析Cookie判断是否能解析到token
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Claims claims = new JwtUtils().parseJWT(jwtProp.getPublicKey(), token);
        // 访问日志
        log.info(DateUtil.now() + " | " + claims.get("roles") + " | " + claims.get("id") + " | " + request.getMethod() + " | " + request.getRequestURI());
        return token;
        // TODO: 2019/11/7 暂无需添加验签
    }
}
