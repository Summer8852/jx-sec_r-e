package com.fantacg.project.config;

import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname GlobalExceptionHandler 全局异常捕获
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (request.getCookies() == null){
            return Result.failure(ResultCode.LOGIN_TOKEN_EXPIRE_ERROR);
        }
        //打印异常信息
        log.error("自定义异常捕获信息：", e);
        //返回自定义异常
        if (e instanceof JxException) {
            JxException jxException = (JxException) e;
            String message = jxException.getExceptionEnum().message();
            log.info("自定义异常捕获信息：" + message);
            return Result.failure(message);
        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }

}

