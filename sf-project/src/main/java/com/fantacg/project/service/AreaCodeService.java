package com.fantacg.project.service;

import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fantacg.project.mapper.AreaCodeMapper;
import com.fantacg.common.pojo.project.AreaCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class AreaCodeService {
    @Autowired
    AreaCodeMapper areaCodeMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void addAreaCode(AreaCode areaCode) {
        areaCodeMapper.insertSelective(areaCode);
    }


    /**
     * 查询城市列表
     *
     * @return
     */
    public Result queryAreaCodeList() {

        try {
            //查询缓存
            String str = redisTemplate.opsForValue().get(KeyConstant.AREA_CODE_LIST);
            if (StringUtil.isNotEmpty(str)) {
                List<AreaCode> list = objectMapper.readValue(str, new TypeReference<List<AreaCode>>(){});
                return Result.success(list);
            }

            //查询数据库
            List<AreaCode> list = areaCodeMapper.selectParentAreaCodeName();
            if (!list.isEmpty()) {
                for (AreaCode areaCode : list) {
                    List<AreaCode> areaCodes1 = areaCodeMapper.selectAreaCodeNameList(areaCode.getCode());
                    areaCode.setChildren(areaCodes1);
                    if (!areaCodes1.isEmpty()) {
                        for (AreaCode areaCode1 : areaCodes1) {
                            List<AreaCode> areaCodes2 = areaCodeMapper.selectAreaCodeNameList(areaCode1.getCode());
                            areaCode1.setChildren(areaCodes2);
                            if (!areaCodes2.isEmpty()) {
                                for (AreaCode areaCode2 : areaCodes2) {
                                    areaCode2.setChildren(new ArrayList<>());
                                }
                            }
                        }
                    }
                }
                //存入缓存redis
                redisTemplate.opsForValue().set(KeyConstant.AREA_CODE_LIST, objectMapper.writeValueAsString(list));
            }
            log.info("查询城市列表:" + list);
            return Result.success(list);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * code 查询地区编码和名称
     *
     * @param code
     * @return
     */
    public Result selectAreaCodeName(String code) {
        try {

            //查询缓存
            String str = redisTemplate.opsForValue().get(KeyConstant.AREA_CODENAME + code);
            if (StringUtil.isNotEmpty(str)) {
                Map<String, Object> map = objectMapper.readValue(str, Map.class);
                return Result.success(map);
            }

            //查询数据库
            Map<String, Object> map = areaCodeMapper.selectAreaCodeName(code);
            if (map != null) {
                //存入缓存redis
                redisTemplate.opsForValue().set(KeyConstant.AREA_CODENAME + code, objectMapper.writeValueAsString(map));
            }
            log.info("查询地区编码和名称:" + map);
            return Result.success(map);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }
}
