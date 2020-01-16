package com.fantacg.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname UploadProperties
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "sf.upload")
public class UploadProperties {

    private String baseUrl;

    private List<String> allowTypes;

    private String https;
}
