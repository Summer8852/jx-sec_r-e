package com.fantacg.upload.service;

import com.fantacg.common.utils.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WordService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface WordService {

    /**
     * 下载模板
     *
     * @param map
     * @param response
     */
    Result download(Map<String, Object> map, HttpServletResponse response);

    String redis();
}
