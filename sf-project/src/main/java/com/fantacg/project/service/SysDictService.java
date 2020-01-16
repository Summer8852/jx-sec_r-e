package com.fantacg.project.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.project.mapper.SysDictMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.pojo.PageResult;

import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.common.dto.project.DictVO;
import com.fantacg.common.pojo.project.SysDict;
import com.fantacg.project.filter.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;

/**
 * 字典表 Service
 * @author DUPENGFEI
 */
@Service
@Slf4j
public class SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询字典表key 类型
     */
    public Result selectDictKeyList() {
        try {
            //查询缓存的数据 如果缓存没有 查询数据库
            String s = redisTemplate.opsForValue().get(KeyConstant.DICT_LIST_TYPE);
            if (StringUtil.isNotEmpty(s)) {
                List<DictVO> dictVOS =  JSON.parseObject(s, List.class);
                return Result.success(dictVOS);
            }
            //查询数据库
            List<DictVO> dictVOS = this.sysDictMapper.selectDictParentList();
            //有数据则 添加到缓存
            if (!dictVOS.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.DICT_LIST_TYPE, objectMapper.writeValueAsString(dictVOS));
            }
            return Result.success(dictVOS);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 分页查询字典表
     *
     * @param page
     * @param rows
     * @param key
     * @return
     */
    public Result selectDictPage(Integer page, Integer rows, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        Map<String, Object> params = new HashMap<>();
        if (StringUtil.isNotEmpty(key)) {
            params.put("type", key);
            params.put("orderByClause", " sort ASC");
        }
        // 查询
        Page<DictVO> pageInfo = this.sysDictMapper.selectDictPage(params);

        for (DictVO sysDict : pageInfo) {
            sysDict.setLabel(sysDict.getDescription());
            List<DictVO> sysDict1 = sysDictMapper.selectDictParentId(sysDict.getId());
            sysDict.setChildren(sysDict1);
            if (!sysDict1.isEmpty()) {
                for (DictVO dict : sysDict1) {
                    List<DictVO> sysDict2 = sysDictMapper.selectDictParentId(dict.getId());
                    dict.setChildren(sysDict2);
                    if (!sysDict2.isEmpty()) {
                        for (DictVO sys : sysDict2) {
                            List<DictVO> sysDict3 = sysDictMapper.selectDictParentId(sys.getId());
                            sys.setChildren(sysDict3);
                        }
                    }
                }
            }
        }
        log.info("分页查询字典表 : " + pageInfo);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 根据type查询字典表
     */
    public Result selectDictByType(String type) {

        //查询缓存redis
        String s = this.redisTemplate.opsForValue().get(KeyConstant.DICT_LIST + type);
        if (StringUtil.isNotEmpty(s)) {
            DictVO dictVO = (DictVO) JSON.parse(s);
            log.info("查询缓存redis字典表 : " + dictVO);
            return Result.success(dictVO);
        }

        //查询数据库
        DictVO dictVO = sysDictMapper.selectDictByType(type);
        if (dictVO != null) {
            List<DictVO> dictVOS = sysDictMapper.selectDictParentId(dictVO.getId());
            dictVO.setChildren(dictVOS);
            if (!dictVOS.isEmpty()) {
                for (DictVO dict : dictVOS) {
                    List<DictVO> dictVOS1 = sysDictMapper.selectDictParentId(dict.getId());
                    dict.setChildren(dictVOS1);
                    if (!dictVOS1.isEmpty()) {
                        for (DictVO sys : dictVOS1) {
                            List<DictVO> dictVOS2 = sysDictMapper.selectDictParentId(sys.getId());
                            sys.setChildren(dictVOS2);
                            if (!dictVOS2.isEmpty()) {
                                for (DictVO vo : dictVOS2) {
                                    vo.setChildren(new ArrayList<>());
                                }
                            }
                        }
                    }
                }
            }
            this.redisTemplate.opsForValue().set(KeyConstant.DICT_LIST + type, JSON.toJSONString(dictVO));
        }
        log.info("根据type查询字典表 : " + dictVO);
        return Result.success(dictVO);

    }


    /**
     * 添加字典表
     */
    public Result installDict(SysDict sysDict) {
        log.info("添加字典表" + sysDict);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        sysDict.setCreateBy(memberId);
        sysDict.setCreateDate(new Date());
        sysDictMapper.insertSelective(sysDict);

        return Result.success(ResultCode.DATA_ADD_SUCCESS.message());
    }

    /**
     * 根据id查询字典表
     * @param id
     * @return
     */
    public Result selectDictById(String id) {
        //查询缓存redis
        String str = redisTemplate.opsForValue().get(KeyConstant.DICT_NAEM_ID + id);
        if (StringUtil.isNotEmpty(str)) {
            HashMap<String, Object> map = JSON.parseObject(str, HashMap.class);
            return Result.success(map);
        }
        HashMap<String, Object> map = sysDictMapper.selectDictById(id);

        //判断是否为空 不为空则添加redis
        if (map != null) {
            redisTemplate.opsForValue().set(KeyConstant.DICT_NAEM_ID + id, JSON.toJSONString(map));
        }
        log.info("查询字典表" + id + ":" + map);
        return Result.success(map);
    }
}


