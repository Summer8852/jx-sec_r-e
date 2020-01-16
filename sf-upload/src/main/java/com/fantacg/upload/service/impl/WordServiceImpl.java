package com.fantacg.upload.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.pojo.project.Word;
import com.fantacg.common.pojo.user.Worker;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.upload.config.JwtProperties;
import com.fantacg.upload.service.WordService;
import com.fantacg.upload.utils.WordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WordServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class WordServiceImpl implements WordService {
    @Value("${file.fileWordPath}")
    private String fileWordPath;

    @Value("${file.ftlPath}")
    private String ftlPath;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 下载附件
     *
     * @param map
     * @param response
     */
    @Override
    public Result download(Map<String, Object> map, HttpServletResponse response) {
        String str = redisTemplate.opsForValue().get(KeyConstant.WORD_ID + map.get("wordId"));
        Word word = JSON.parseObject(str, Word.class);
        // 文档模板路劲
        String wordPath = word.getWordPath();
        // 文档名称
        String wordName = word.getWordName();
        // 文档类型
        String wordType = word.getWordType();
        // url
        String url = new IdWorker().nextId() + "/" + wordName + "." + wordType;
        // 文件路径（存储在服务器的路劲）
        String filePath = fileWordPath + url;
        // 创建文档
        WordUtil.createWord(map, ftlPath, wordPath, filePath);
        // 下载文档
//        WordUtil.downLoadFile(filePath, response);

        return Result.success("file/word/" + url);
    }


    @Override
    public String redis() {
        System.out.println("redis");
        redisTemplate.delete(redisTemplate.keys(KeyConstant.INDEX_VIDEO_LIST + "*"));
        System.out.println("redis");
        return "redis";
    }


}
