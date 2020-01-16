package com.fantacg.project.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.PersonGoodCreditInfoMapper;
import com.fantacg.common.pojo.project.PersonGoodCreditInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PersonGoodCreditInfoService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class PersonGoodCreditInfoService {

    @Autowired
    PersonGoodCreditInfoMapper personGoodCreditInfoMapper;


    /**
     * 分页查询良好行为记录列表
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param searchCateId
     * @return
     */
    public Result queryProjectByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key, String searchCateId) {

        log.info("分页查询良好行为记录列表");
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        // 开始分页
        PageHelper.startPage(page, rows);
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        if (StringUtils.isNotBlank(key)) {
            params.put("name", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(searchCateId)) {
            params.put("searchCateId", searchCateId);
        }
        // 查询
        Page<PersonGoodCreditInfo> pageInfo = this.personGoodCreditInfoMapper.selectAllPersonGoodCreditInfoByPage(params);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }


    /**
     * 添加良好行为记录
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addPersonGoodCreditInfo(PersonGoodCreditInfo personGoodCreditInfo) {
        log.info("添加良好行为记录:" + personGoodCreditInfo);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        personGoodCreditInfo.setId(new IdWorker().nextId());
        personGoodCreditInfo.setCreateDate(new Date());
        personGoodCreditInfo.setInUserName(memberId);
        personGoodCreditInfo.setInDate(new Date());
        personGoodCreditInfo.setIsDel(0);
        int i = this.personGoodCreditInfoMapper.insertSelective(personGoodCreditInfo);
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }


}
