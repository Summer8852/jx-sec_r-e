package com.fantacg.common.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname Check 参数校验 注解
 * @Created by Dupengfei 2020-01-15 13:52
 * @Version 2.0
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RUNTIME)
public @interface Check {
    // 字段校验规则，格式：字段名+校验规则+冒号+错误信息，例如：id<10:ID必须少于10
    String[] value();

}
