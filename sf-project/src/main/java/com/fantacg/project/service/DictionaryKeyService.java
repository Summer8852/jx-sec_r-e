package com.fantacg.project.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.NumberCostant;
import com.fantacg.common.dto.project.DictVO;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.project.DictionaryKey;
import com.fantacg.common.utils.Result;
import com.fantacg.project.mapper.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DictionaryKeyService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Service
public class DictionaryKeyService {

    @Autowired
    DictionaryKeyMapper dictionaryKeyMapper;
    @Autowired
    DictionaryMapper dictionaryMapper;
    @Autowired
    DictionaryTradeTypeMapper dictionaryTradeTypeMapper;
    @Autowired
    DictionaryTradeTypeBoundMapper dictionaryTradeTypeBoundMapper;
    @Autowired
    DictionaryCertTitleLevelMapper dictionaryCertTitleLevelMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询字典表key 类型
     */
    public Result selectDictionaryKeyList() {

        try {
            //查询缓存的数据 如果缓存没有 查询数据库
            String s = redisTemplate.opsForValue().get(KeyConstant.DICTIONARY_KEY);
            if (StringUtil.isNotEmpty(s)) {
                List<DictionaryKey> list = objectMapper.readValue(s, new TypeReference<List<DictionaryKey>>() {
                });
                return Result.success(list);
            }

            //查询数据库
            Example example = new Example(DictionaryKey.class);
            example.createCriteria().andCondition(" del_flag = 0 ");
            List<DictionaryKey> list = this.dictionaryKeyMapper.selectByExample(example);

            //有数据则 添加到缓存
            if (!list.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.DICTIONARY_KEY, JSON.toJSONString(list));
            }
            return Result.success(list);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 根据type 查询
     *
     * @param type
     * @return
     */
    public Result selectDictByType(String type) {
        List<DictVO> dictVOS = this.queryDictionaryListAll(type);
        List<DictVO> collect = dictVOS.stream().filter(s -> NumberCostant.PARENT.equals(s.getParentId())).collect(Collectors.toList());
        for (DictVO dictVO : collect) {
            List<DictVO> collect1 = dictVOS.stream().filter(s -> dictVO.getValue().equals(s.getParentId())).collect(Collectors.toList());
            dictVO.setChildren(collect1);
            for (DictVO dictVO1 : collect1) {
                List<DictVO> collect2 = dictVOS.stream().filter(s -> dictVO1.getValue().equals(s.getParentId())).collect(Collectors.toList());
                dictVO1.setChildren(collect2);
                if (!collect2.isEmpty()) {
                    for (DictVO dictVO2 : collect2) {
                        dictVO2.setChildren(new ArrayList<>());
                    }
                }
            }
        }
        return Result.success(collect);
    }


    /**
     * 查询所有字典
     *
     * @return
     */
    private List<DictVO> queryDictionaryListAll(String type) {
        try {

            //查询redis 缓存
            String str = redisTemplate.opsForValue().get(KeyConstant.DICTIONARY_TYPE_KEY + type);
            if (StringUtil.isNotEmpty(str)) {
                return objectMapper.readValue(str, new TypeReference<List<DictVO>>() {
                });
            }

            //查询数据库
            List<DictVO> dictVOS = this.dictionaryMapper.selectDictByType(type);
            if (!dictVOS.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.DICTIONARY_TYPE_KEY + type, JSON.toJSONString(dictVOS));
                return dictVOS;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 根据type,value查询
     *
     * @param type
     * @param value
     * @return
     */
    public Result getDictionaryByValue(String type, String value) {
        try {
            String str = redisTemplate.opsForValue().get(KeyConstant.DICTIONARY_TYPE_KEY + type + ":" + value);
            if (StringUtil.isNotEmpty(str)) {
                DictVO dictVO = objectMapper.readValue(str, DictVO.class);
                return Result.success(dictVO);
            }

            List<DictVO> dictVOS = this.queryDictionaryListAll(type);
            List<DictVO> collect = dictVOS.stream().filter(s -> value.equals(s.getValue())).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                if (!(NumberCostant.PARENT).equals(collect.get(0).getParentId())) {
                    List<DictVO> collect1 = dictVOS.stream().filter(s -> collect.get(0).getParentId().equals(s.getValue())).collect(Collectors.toList());
                    collect.get(0).setChildren(collect1);
                    if (!collect1.isEmpty()) {
                        if (!NumberCostant.PARENT.equals(collect1.get(0).getParentId())) {
                            List<DictVO> collect2 = dictVOS.stream().filter(s -> collect1.get(0).getParentId().equals(s.getValue())).collect(Collectors.toList());
                            collect1.get(0).setChildren(collect2);
                            collect2.get(0).setChildren(new ArrayList<>());
                        } else {
                            collect1.get(0).setChildren(new ArrayList<>());
                        }
                    }
                } else {
                    collect.get(0).setChildren(new ArrayList<>());
                }

                redisTemplate.opsForValue().set(KeyConstant.DICTIONARY_TYPE_KEY + type + ":" + value, JSON.toJSONString(collect.get(0)));
                return Result.success(collect.get(0));
            }
            return Result.success();
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 企业资质资格专业类别字典表 列表
     *
     * @param parentId
     * @return
     */
    public Result getDicTradeTypeBoundByParentId(String parentId) throws Exception {
        //查询redis 缓存
        List<DictVO> dictVOS = this.dicTradeTypeBoundList();
        List<DictVO> collect = dictVOS.stream().filter(s -> parentId.equals(s.getParentId())).collect(Collectors.toList());
        for (DictVO dictVO : collect) {
            List<DictVO> collect1 = dictVOS.stream().filter(s -> dictVO.getValue().equals(s.getParentId())).collect(Collectors.toList());
            dictVO.setChildren(collect1);
            for (DictVO dictVO1 : collect1) {
                List<DictVO> collect2 = dictVOS.stream().filter(s -> dictVO1.getValue().equals(s.getParentId())).collect(Collectors.toList());
                if (!collect2.isEmpty()) {
                    for (DictVO vo : collect2) {
                        vo.setChildren(new ArrayList<>());
                    }
                }
                dictVO1.setChildren(collect2);
            }
        }

        return Result.success(collect);

    }

    /**
     * 根据value 查询 企业资质资格专业类别
     *
     * @param value
     * @return
     */
    public Result getDicTradeTypeBoundByV(String value) throws Exception {
        List<DictVO> dictVOS = this.dicTradeTypeBoundList();
        List<DictVO> collect = dictVOS.stream().filter(s -> value.equals(s.getValue())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            if (!NumberCostant.PARENT.equals(collect.get(0).getParentId())) {
                List<DictVO> collect1 = dictVOS.stream().filter(s -> collect.get(0).getParentId().equals(s.getValue())).collect(Collectors.toList());
                collect.get(0).setChildren(collect1);
                if (!collect1.isEmpty()) {
                    if (!NumberCostant.PARENT.equals(collect1.get(0).getParentId())) {
                        List<DictVO> collect2 = dictVOS.stream().filter(s -> collect1.get(0).getParentId().equals(s.getValue())).collect(Collectors.toList());
                        collect1.get(0).setChildren(collect2);
                        if (!collect2.isEmpty()) {
                            collect2.get(0).setChildren(new ArrayList<>());
                        }
                    } else {
                        collect1.get(0).setChildren(new ArrayList<>());
                    }
                } else {
                    collect.get(0).setChildren(new ArrayList<>());
                }
            } else {
                collect.get(0).setChildren(new ArrayList<>());
            }
            return Result.success(collect.get(0));
        }
        return Result.success();
    }

    /**
     * 查询所有未删除的企业资质资格专业类别字典表
     *
     * @return
     */
    private List<DictVO> dicTradeTypeBoundList() throws Exception {

        String str = redisTemplate.opsForValue().get(KeyConstant.DICTIONARY_TRADETYPEBOUND_KEY);
        if (StringUtil.isNotEmpty(str)) {
            return objectMapper.readValue(str, new TypeReference<List<DictVO>>() {
            });
        }

        List<DictVO> dictVOS = this.dictionaryTradeTypeBoundMapper.dicTradeTypeBoundList();
        if (dictVOS.isEmpty()) {
            return Collections.emptyList();
        }

        redisTemplate.opsForValue().set(KeyConstant.DICTIONARY_TRADETYPEBOUND_KEY, JSON.toJSONString(dictVOS));
        return dictVOS;
    }


    /**
     * 企业资质等级 列表
     *
     * @param parentId
     * @return
     */
    public Result getCertTitleLevelByParentId(String parentId) throws Exception {
        List<DictVO> dictVOS = this.dicCertTitleLevelList();
        List<DictVO> collect = dictVOS.stream().filter(s -> parentId.equals(s.getParentId())).collect(Collectors.toList());
        for (DictVO dictVO : collect) {
            List<DictVO> collect1 = dictVOS.stream().filter(s -> dictVO.getValue().equals(s.getParentId())).collect(Collectors.toList());
            dictVO.setChildren(collect1);
            for (DictVO dictVO1 : collect1) {
                List<DictVO> collect2 = dictVOS.stream().filter(s -> dictVO1.getValue().equals(s.getParentId())).collect(Collectors.toList());
                dictVO1.setChildren(collect2);
            }
        }
        return Result.success(collect);
    }

    /**
     * 根据parentId value 查询 企业资质等级
     *
     * @param parentId
     * @param value
     * @return
     */
    public Result getCertTitleLevelByValue(String parentId, String value) throws Exception {
        List<DictVO> dictVOS = this.dicCertTitleLevelList();
        List<DictVO> collect = dictVOS.stream().filter(s -> value.equals(s.getValue()) && parentId.equals(s.getParentId())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            if (!NumberCostant.PARENT.equals(collect.get(0).getParentId())) {
                List<DictVO> collect1 = dictVOS.stream().filter(s -> collect.get(0).getParentId().equals(s.getValue())).collect(Collectors.toList());
                collect.get(0).setChildren(collect1);
                if (!collect1.isEmpty()) {
                    if (!NumberCostant.PARENT.equals(collect1.get(0).getParentId())) {
                        List<DictVO> collect2 = dictVOS.stream().filter(s -> collect1.get(0).getParentId().equals(s.getValue())).collect(Collectors.toList());
                        collect1.get(0).setChildren(collect2);
                        collect2.get(0).setChildren(new ArrayList<>());
                    } else {
                        collect1.get(0).setChildren(new ArrayList<>());
                    }
                }
            } else {
                collect.get(0).setChildren(new ArrayList<>());
            }
            return Result.success(collect.get(0));
        }
        return Result.success();
    }

    /**
     * 查询所有未删除的企业资质资格专业类别字典表
     *
     * @return
     */
    private List<DictVO> dicCertTitleLevelList() throws Exception {

        String str = redisTemplate.opsForValue().get(KeyConstant.DICTIONARY_CERTTITLELEVEL_KEY);
        if (StringUtil.isNotEmpty(str)) {
            return objectMapper.readValue(str, new TypeReference<List<DictVO>>() {
            });
        }

        List<DictVO> dictVOS = this.dictionaryCertTitleLevelMapper.dicCertTitleLevelList();
        if (dictVOS.isEmpty()) {
            return Collections.emptyList();
        }

        redisTemplate.opsForValue().set(KeyConstant.DICTIONARY_CERTTITLELEVEL_KEY, JSON.toJSONString(dictVOS));
        return dictVOS;
    }
}
