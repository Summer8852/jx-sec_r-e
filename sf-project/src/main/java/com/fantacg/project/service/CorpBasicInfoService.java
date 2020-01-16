package com.fantacg.project.service;

import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.AreaCodeMapper;
import com.fantacg.project.mapper.CorpBasicCertMapper;
import com.fantacg.project.mapper.CorpBasicInfoMapper;
import com.fantacg.project.mapper.CorpCertInfoManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.dto.project.CorpBasicInfos;
import com.fantacg.common.pojo.project.CorpBasicCert;
import com.fantacg.common.pojo.project.CorpBasicInfo;
import com.fantacg.common.pojo.project.CorpCertInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CorpBasicInfoService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class CorpBasicInfoService {

    @Autowired
    CorpBasicInfoMapper corpBasicInfoMapper;
    @Autowired
    CorpCertInfoManager corpCertInfoManager;
    @Autowired
    CorpBasicCertMapper corpBasicCertMapper;
    @Autowired
    AreaCodeMapper areaCodeMapper;
    @Autowired
    ObjectMapper objectMapper;


    /**
     * * 添加企业信息 (企业名称必填)
     * * @param corpBasicInfos
     * * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addCorpBasicInfo(CorpBasicInfos corpBasicInfos) {
        log.info("添加企业信息");
        String codeId = null;
        int i ;
        //判断企业名称是否填写
        if (StringUtil.isEmpty(corpBasicInfos.getCorpBasicInfo().getCorpName())) {
            return Result.failure("请填写企业名称");
        }

        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        CorpBasicInfo corpBasicInfo = corpBasicInfos.getCorpBasicInfo();

        //判断社会统一代码是否填写
        log.info("判断社会统一代码是否填写");
        if (StringUtils.isNoneEmpty(corpBasicInfo.getCorpCode())) {
            //查询我的企业 是否有相同的企业编号 如有 则返回提示
            log.info("查询我的企业 是否有相同的企业编号 如有 则返回提示");
            codeId = this.corpBasicInfoMapper.selectCorpBasicInfoCode(memberId, corpBasicInfo.getCorpCode());
            if (StringUtil.isNotEmpty(codeId)) {
                return Result.failure(ResultCode.CORPCODE_ERROR);
            }
        }

        //判断企业名称是否重复 如有重复则返回提示
        log.info("判断企业名称是否重复 如有重复则返回提示");
        if (StringUtils.isNoneEmpty(corpBasicInfo.getCorpName())) {
            codeId = this.corpBasicInfoMapper.selectCorpBasicInfoName(memberId, corpBasicInfo.getCorpName());
            if (StringUtil.isNotEmpty(codeId)) {
                return Result.failure(ResultCode.CORPNAME_ERROR);
            }
        }

        //企业基本信息
        log.info("添加企业基本信息");
        corpBasicInfo.setId(new IdWorker().nextId());
        corpBasicInfo.setInDate(new Date());
        corpBasicInfo.setInUserName(memberId);
        i = this.corpBasicInfoMapper.installCorpBasicInfo(corpBasicInfo);

        //企业资质数据表
        corpBasicInfos.getCorpCertInfos();
        if (null != corpBasicInfos.getCorpCertInfos()) {
            for (CorpCertInfo corpcertinfo : corpBasicInfos.getCorpCertInfos()) {
                //添加企业资质数据
                log.info("添加企业资质数据");
                corpcertinfo.setId(new IdWorker().nextId());
                corpcertinfo.setCorpCode(corpBasicInfo.getCorpCode());
                corpcertinfo.setCorpName(corpBasicInfo.getCorpName());
                corpcertinfo.setInUserName(memberId);
                corpcertinfo.setInDate(new Date());
                this.corpCertInfoManager.insertSelective(corpcertinfo);

                //企业基本信息信息与企业资质关系表
                log.info("企业基本信息信息与企业资质关系表");
                CorpBasicCert corpBasicCert = new CorpBasicCert();
                corpBasicCert.setId(new IdWorker().nextId());
                corpBasicCert.setCorpBasicId(corpBasicInfo.getId());
                corpBasicCert.setCorpCertId(String.valueOf(corpcertinfo.getId()));
                corpBasicCert.setInMemberId(memberId);
                corpBasicCert.setInDate(new Date());
                this.corpBasicCertMapper.insertSelective(corpBasicCert);
            }
        }
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * *分页查询企业信息
     * *@return
     */
    public Result queryCorpBasicInfoList(Integer page, Integer rows, String sortBy, Boolean desc, String key, String corpType,String searchCateId) {
        log.info("分页查询企业信息:" + page + " - " + rows + " - " + sortBy + " - " + desc + " - " + searchCateId);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        // 开始分页
        PageHelper.startPage(page, rows);
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        if (StringUtils.isNotBlank(key)) {
            params.put("corpName", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(key)) {
            params.put("corpType", corpType);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            params.put("orderByClause", orderByClause);
        }
        // 查询
        Page<CorpBasicInfo> pageInfo = (Page<CorpBasicInfo>) corpBasicInfoMapper.queryCorpBasicInfoListPage(params);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }


    /**
     ** 删除企业
     ** @param id
     ** @return
     */
    public Result removeCorpBasicInfo(Long id) {
        log.info("删除企业:" + id);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        CorpBasicInfo corpBasicInfo = new CorpBasicInfo();
        corpBasicInfo.setEditUserName(memberId);
        corpBasicInfo.setEditDate(new Date());
        corpBasicInfo.setIsDel(1);
        Example example = new Example(CorpBasicCert.class);
        example.createCriteria().andCondition(" id = " + id + " AND in_user_name = " + memberId);
        int i = corpBasicInfoMapper.updateByExampleSelective(corpBasicInfo, example);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * * 查询企业详情
     */
    public Result selectCorpBasicInfoDetail(Long id) {
        log.info("查询企业详情:" + id);
        try {
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            CorpBasicInfos corpBasicInfos = new CorpBasicInfos();
            //查询企业详情
            CorpBasicInfo corpBasicInfo = this.corpBasicInfoMapper.queryCorpBasicInfoById(id, memberId);
            corpBasicInfos.setCorpBasicInfo(corpBasicInfo);

            //查询资质
            Example example = new Example(CorpBasicCert.class);
            example.createCriteria().andCondition(" corp_basic_id = " + id + " AND in_member_id = " + memberId);
            List<CorpBasicCert> corpBasicCerts = this.corpBasicCertMapper.selectByExample(example);
            if (!corpBasicCerts.isEmpty()) {
                List<CorpCertInfo> corpCertInfos = new ArrayList<>();
                for (CorpBasicCert corpBasicCert : corpBasicCerts) {
                    CorpCertInfo corpCertInfo = this.corpBasicInfoMapper.queryCorpCertInfoById(Long.valueOf(corpBasicCert.getCorpCertId()));
                    corpCertInfos.add(corpCertInfo);
                }
                corpBasicInfos.setCorpCertInfos(corpCertInfos);
            }
            return Result.success(corpBasicInfos);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }


    /**
     * * 修改企业基本信息 及 修改资质或添加资质
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateBasicInfo(CorpBasicInfos corpBasicInfos) {

        log.info("修改企业基本信息 及 修改资质或添加资质");
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        //判断是否传id
        int i = 0;
        if (corpBasicInfos.getCorpBasicInfo().getId() == null) {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        //判断社会统一代码是否填写
        if (StringUtils.isNoneEmpty(corpBasicInfos.getCorpBasicInfo().getCorpCode())) {
            //查询我的企业 是否有相同的企业编号 如有 则返回提示
            String codeId = this.corpBasicInfoMapper.selectCorpBasicInfoCode(memberId, corpBasicInfos.getCorpBasicInfo().getCorpCode());
            if (StringUtil.isNotEmpty(codeId) && !codeId.equals(corpBasicInfos.getCorpBasicInfo().getId().toString())) {
                    return Result.failure(ResultCode.CORPCODE_ERROR);
            }
        }
        //判断企业名称是否重复 如有重复则返回提示
        if (StringUtils.isNoneEmpty(corpBasicInfos.getCorpBasicInfo().getCorpName())) {
            String codeId = this.corpBasicInfoMapper.selectCorpBasicInfoName(memberId, corpBasicInfos.getCorpBasicInfo().getCorpName());
            if (StringUtil.isNotEmpty(codeId) && !codeId.equals(corpBasicInfos.getCorpBasicInfo().getId().toString())) {
                    return Result.failure(ResultCode.CORPNAME_ERROR);
            }
        }

        //修改企业信息
        if (corpBasicInfos.getCorpBasicInfo() != null) {
            corpBasicInfos.getCorpBasicInfo().setEditUserName(memberId);
            corpBasicInfos.getCorpBasicInfo().setEditDate(new Date());
            corpBasicInfos.getCorpBasicInfo().setInUserName(memberId);
            i = this.corpBasicInfoMapper.updateCorpBasicInfo(corpBasicInfos.getCorpBasicInfo());
        }

        if (!corpBasicInfos.getCorpCertInfos().isEmpty()) {
            log.info("修改资质");
            //修改资质
            if (!corpBasicInfos.getCorpCertInfos().isEmpty()) {
                for (CorpCertInfo corpCertInfo : corpBasicInfos.getCorpCertInfos()) {
                    if (corpCertInfo.getId() != null) {
                        log.info("修改资质");
                        corpCertInfo.setCorpCode(corpBasicInfos.getCorpBasicInfo().getCorpCode());
                        corpCertInfo.setCorpName(corpBasicInfos.getCorpBasicInfo().getCorpName());
                        corpCertInfo.setEditDate(new Date());
                        corpCertInfo.setEditUserName(memberId);
                        Example example = new Example(CorpCertInfo.class);
                        example.createCriteria().andCondition(" id = " + corpCertInfo.getId() + " AND in_user_name = " + memberId);
                        i = this.corpCertInfoManager.updateByExampleSelective(corpCertInfo, example);
                    } else {
                        log.info("添加资质");
                        corpCertInfo.setId(new IdWorker().nextId());
                        corpCertInfo.setCorpCode(corpBasicInfos.getCorpBasicInfo().getCorpCode());
                        corpCertInfo.setCorpName(corpBasicInfos.getCorpBasicInfo().getCorpName());
                        corpCertInfo.setInUserName(memberId);
                        corpCertInfo.setInDate(new Date());
                        this.corpCertInfoManager.insertSelective(corpCertInfo);
                        log.info("企业基本信息信息与企业资质关系表");
                        CorpBasicCert corpBasicCert = new CorpBasicCert();
                        corpBasicCert.setId(new IdWorker().nextId());
                        corpBasicCert.setCorpBasicId(corpBasicInfos.getCorpBasicInfo().getId());
                        corpBasicCert.setCorpCertId(String.valueOf(corpCertInfo.getId()));
                        corpBasicCert.setInMemberId(memberId);
                        corpBasicCert.setInDate(new Date());
                        i = this.corpBasicCertMapper.insertSelective(corpBasicCert);
                    }
                }
            }
        }
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }


    /**
     * * 模糊查询自己的企业信息
     */
    public Result searchCorpBasicInfo(String corpName) {
        log.info("模糊查询自己的企业信息:" + corpName);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        HashMap<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("corpName", corpName);
        List<Map<String, Object>> maps = this.corpBasicInfoMapper.searchCorpBasicInfo(params);
        if (!maps.isEmpty()) {
            for (Map<String, Object> map : maps) {
                map.put("id", String.valueOf(map.get("id")));
            }
        }
        return Result.success(maps);
    }

}
