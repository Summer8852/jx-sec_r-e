package com.fantacg.common.exception;

import com.fantacg.common.enums.ExceptionEnum;
import lombok.Getter;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname JxException 自定义异常类
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Getter
public class JxException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public JxException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }


}
