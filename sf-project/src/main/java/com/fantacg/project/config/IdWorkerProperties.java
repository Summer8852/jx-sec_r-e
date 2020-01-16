package com.fantacg.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname IdWorkerProperties
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@ConfigurationProperties(prefix = "sf.worker")
public class IdWorkerProperties {
    /**
     * 当前机器id
     */
    private long workerId;
    /**
     * 序列号
     */
    private long dataCenterId;
}