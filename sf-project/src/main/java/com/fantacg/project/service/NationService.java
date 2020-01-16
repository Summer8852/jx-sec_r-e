package com.fantacg.project.service;

import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.utils.Result;
import com.fantacg.project.mapper.NationMapper;
import com.fantacg.common.pojo.project.Nation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname NationService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class NationService {

    @Autowired
    NationMapper nationMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;


    /**
     * 添加世界各国和地区名称代码
     *
     * @param nation
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addNation(Nation nation) {
        nationMapper.insert(nation);
        return Result.success();
    }


    /**
     * 查询世界各国和地区名称代码
     *
     * @return
     */
    public Result queryNation(){

        try {
            //查询redis
            String str = redisTemplate.opsForValue().get(KeyConstant.NATION_LIST);
            if (StringUtil.isNotEmpty(str)) {
                List<Nation> nations = objectMapper.readValue(str, List.class);
                log.info("世界各国和地区名称代码 redis :" + nations);
                return Result.success(nations);
            }

            //查询数据库
            List<Nation> nations = nationMapper.selectAll();
            if (!nations.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.NATION_LIST, objectMapper.writeValueAsString(nations));
            }
            log.info("世界各国和地区名称代码:" + nations);
            return Result.success(nations);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);

        }
    }

    /**
     * 查询世界各国和地区名称代码
     *
     * @return
     */
    public Result queryNationById(Long id) {
        try {
            //查询redis
            String str = redisTemplate.opsForValue().get(KeyConstant.NATION_ID + id);
            if (StringUtil.isNotEmpty(str)) {
                Nation nation = objectMapper.readValue(str, Nation.class);
                log.info("世界各国和地区名称代码 redis :" + nation);
                return Result.success(nation);
            }
            //查询数据库
            Nation nation = new Nation();
            nation.setId(id);
            nation = nationMapper.selectOne(nation);
            if (nation != null) {
                redisTemplate.opsForValue().set(KeyConstant.NATION_ID + id, objectMapper.writeValueAsString(nation));
            }
            log.info("世界各国和地区名称代码:" + nation);
            return Result.success(nation);
        } catch (IOException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }
}
