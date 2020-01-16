package com.fantacg.project.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.PersonBadCreditInfoMapper;
import com.fantacg.common.pojo.project.PersonBadCreditInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname PersonBadCreditInfoService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class PersonBadCreditInfoService {


    @Autowired
    PersonBadCreditInfoMapper personBadCreditInfoMapper;

    /**
     * 分页查询不良行为记录
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @param searchCateId
     * @return
     */
    public Result queryPersonBadCreditByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key, String searchCateId) {

        log.info("分页查询不良行为记录列表");
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
        Page<PersonBadCreditInfo> pageInfo = this.personBadCreditInfoMapper.queryPersonBadCreditByPage(params);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 添加 不良行为记录
     *
     * @param personBadCreditInfo
     * @return
     */
    public Result addPersonBadCreditInfo(PersonBadCreditInfo personBadCreditInfo) {
        log.info("添加不良行为记录:" + personBadCreditInfo);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        personBadCreditInfo.setId(new IdWorker().nextId());
        personBadCreditInfo.setCreateDate(new Date());
        personBadCreditInfo.setInUserName(memberId);
        personBadCreditInfo.setInDate(new Date());
        personBadCreditInfo.setIsDel(0);
        int i = this.personBadCreditInfoMapper.insertSelective(personBadCreditInfo);
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }
}
