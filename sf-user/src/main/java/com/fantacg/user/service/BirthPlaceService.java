package com.fantacg.user.service;

import cn.hutool.core.util.IdcardUtil;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.pojo.worker.BirthPlace;
import com.fantacg.user.mapper.BirthPlaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname BirthPlaceService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
public class BirthPlaceService {

    private static final int CARDNUM = 6;
    @Autowired
    BirthPlaceMapper birthPlaceMapper;
    @Autowired
    StringRedisTemplate redisTemplate;


    /**
     * @param cardNum 身份证Id
     * @return
     */
    public String queryBirthPlaceById(String cardNum) {
        String name = "";
        if (IdcardUtil.isValidCard(cardNum)) {
            name = redisTemplate.opsForValue().get(KeyConstant.BIRTH_PLACE_KEY_PREFIX + cardNum.substring(0, CARDNUM));
            if (StringUtil.isEmpty(name)) {
                BirthPlace birthPlace = birthPlaceMapper.selectByPrimaryKey(cardNum.substring(0, CARDNUM));
                name = birthPlace.getName();
                if (StringUtil.isNotEmpty(name)) {
                    redisTemplate.opsForValue().set(KeyConstant.BIRTH_PLACE_KEY_PREFIX + cardNum.substring(0, CARDNUM), name);
                }
            }
        }
        return name;
    }


}
