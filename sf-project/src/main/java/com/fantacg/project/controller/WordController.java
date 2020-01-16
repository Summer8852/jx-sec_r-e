package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.common.pojo.project.Word;
import com.fantacg.project.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/word")
public class WordController {
    @Autowired
    WordService wordService;


    /**
     * 查询所有模板文档
     *
     * @return 所有文档列表
     */
    @GetMapping("/list")
    public Result wordList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return this.wordService.wordList(page,rows);
    }

    /**
     * 添加模板文档
     *
     * @param word 模板信息
     * @return 添加成功/失败
     */
    @PostMapping
    public Result addWord(@RequestBody Word word) {
        return this.wordService.addWorder(word);
    }

    /**
     * 修改模板文档
     *
     * @param word 需要修改的模板信息
     * @return 修改成功/失败
     */
    @PostMapping("/updateWord")
    public Result updateWord(@RequestBody Word word) {
        return this.wordService.updateWord(word);
    }
}
