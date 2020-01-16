package com.fantacg.project.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.project.Word;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.WordMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname WordService 模板文档
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
public class WordService {

    @Autowired
    WordMapper wordMapper;
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 查询所有模板文档
     *
     * @return 所有文档列表
     */
    public Result wordList(Integer page,Integer rows) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 查询
        Page<Word> pageInfo = (Page<Word>) this.wordMapper.selectAll();
        //跟新redis
        this.reidsWord();
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 添加模板文档
     *
     * @param word 模板信息
     * @return 添加成功/失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addWorder(Word word) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        word.setId(new IdWorker().nextId());
        word.setWordNo(String.valueOf(new IdWorker().nextId()));
        word.setIntUserName(memberId);
        word.setInDate(new Date());
        int i = this.wordMapper.insertSelective(word);
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 修改模板文档
     *
     * @param word 需要修改的模板信息
     * @return 修改成功/失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateWord(Word word) {
        int i = this.wordMapper.updateByPrimaryKey(word);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }


    /**
     * 将模板信息添加到redis
     */
    private void reidsWord() {
        //1.查询所有模板
        List<Word> words = this.wordMapper.selectAll();
        for (Word word : words) {
            //2.添加/替换最新redis
            redisTemplate.opsForValue().set(KeyConstant.WORD_ID + word.getId(), JSON.toJSONString(word));
        }
    }

}
