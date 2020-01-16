package com.fantacg.upload.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * EnableMBeanExport 解决jmx重复注册bean的问题
 * @author DUPENGFEI
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 *  EnableMBeanExport 解决jmx重复注册bean的问题
 * @author 智慧安全云
 * @Classname FastClientImporter
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Configuration
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {
}