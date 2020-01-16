package com.fantacg.upload.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.upload.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WordController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/upload")
public class WordController {

    @Autowired
    WordService wordService;

    /**
     * 下载通用文档模板
     */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public Result download(@RequestBody Map<String, Object> map, HttpServletResponse response) {
        return this.wordService.download(map, response);
    }

    /**
     * 下载通用文档模板
     */
    @GetMapping("/redis")
    public String redis() {
        return this.wordService.redis();
    }


}
