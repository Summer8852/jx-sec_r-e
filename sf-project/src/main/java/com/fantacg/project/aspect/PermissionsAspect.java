package com.fantacg.project.aspect;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.user.Menu;
import com.fantacg.common.utils.Result;
import com.fantacg.project.client.UserClient;
import com.fantacg.project.filter.LoginInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname  PermissionsAspect 权限处理，切面处理类
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Aspect
@Component
public class PermissionsAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserClient userClient;

    @Pointcut("@annotation(com.fantacg.project.aspect.Requirespermissions)")
    public void Requirespermissions() {

    }

    @Before("Requirespermissions()")
    public void Requirespermissions(JoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Requirespermissions requirespermissionsFilter = signature.getMethod().getAnnotation(Requirespermissions.class);
        //权限校验

        Claims claims = LoginInterceptor.getLoginClaims();
        if (claims != null) {
            //管理员不校验权限 如果是用户
            if (RoleConstant.MEMBER.equals(claims.get("roles"))) {
                Result r =this.userClient.getMemeberAuthInfo();
                //用户未认证
                if(r.getCode()!=200){
                    //抛出异常中断请求
                    throw new JxException(ExceptionEnum.NO_PREMISSION);
                }

                String key = KeyConstant.PERMISS_KEY_PREFIX+ ObjectUtils.toLong(claims.get(JwtConstans.JWT_KEY_ID));
                String   permission = this.redisTemplate.opsForValue().get(key);
                //存入权限
                //redis没有则从数据库查按钮权限，存入redis
                if(StringUtils.isBlank(permission)){
                    Result result = this.userClient.doGetAuthorizationInfo();
                    StringBuffer sb=new StringBuffer();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if(result.getData() != null ){
                        String s = objectMapper.writeValueAsString(result.getData());
                        List<Object> list = objectMapper.readValue(s, List.class);
                        for(Object m:list){
                            String ms = objectMapper.writeValueAsString(m);
                            Menu menu = objectMapper.readValue(ms, Menu.class);
                            appendAllPermission(sb, menu);
                        }
                    }
                    if(sb.length()>1){
                        sb.deleteCharAt(sb.length()-1);
                    }
                    //权限缓存30分钟
                    this.redisTemplate.opsForValue().set(key, sb.toString(), 30, TimeUnit.MINUTES);
                    permission=sb.toString();
                }

                if(!permission.contains(requirespermissionsFilter.value())){
                    //抛出异常中断请求
                    throw new JxException(ExceptionEnum.NO_PREMISSION);
                }
            }
        }

    }

    private void appendAllPermission(StringBuffer sb, Menu m) {
        if(m.getItems()!=null){
            for(Menu menu:m.getItems()){
                sb.append(menu.getPerms()+",");
                appendAllPermission(sb,menu);
            }
        }
    }
}
