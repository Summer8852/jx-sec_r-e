package com.fantacg.project.service;


import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.project.*;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname TeamMasterService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class TeamMasterService {

    @Autowired
    TeamMasterMapper teamMasterMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CorpBasicInfoMapper corpBasicInfoMapper;
    @Autowired
    CorpBasicCertMapper corpBasicCertMapper;
    @Autowired
    ProjectInfoMapper projectInfoMapper;
    @Autowired
    FileAttachmentInfoMapper fileAttachmentInfoMapper;
    @Autowired
    ProjectTmCorpMapper projectTmCorpMapper;


    /**
     * 添加班组
     * 同一项目上的班主名称不能相同
     *
     * @param teamMaster
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result installTeamMaster(TeamMaster teamMaster) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        long nextId = new IdWorker().nextId();
        int num = 0;

        //判断班组名是否重复
        String id = teamMasterMapper.selectByName(memberId, teamMaster.getTeamName());
        if (StringUtil.isNotEmpty(id)) {
            return Result.failure("班组名称不能重复");
        }

        //修改 或 添加 企业信息
        String corpId = teamMaster.getCorpId();
        String corpCode = teamMaster.getCorpCode();
        String corpName = teamMaster.getCorpName();
        corpId = saveCorpBasic(corpId, corpCode, corpName, memberId);

        //添加班组信息 (同一个项目上的班组名称不能重复)
        teamMaster.setId(String.valueOf(nextId));
        teamMaster.setInUserName(memberId);
        teamMaster.setInDate(new Date());
        teamMaster.setTeamSysNo("NO-" +nextId);
        teamMaster.setCorpId(corpId);
        teamMasterMapper.installTeamMasters(teamMaster);

        //添加班组与企业的关系表
        ProjectTmCorp ptc = new ProjectTmCorp();
        ptc.setTramMasterId(teamMaster.getId());
        ptc.setCorpBasicId(corpId);
        ptc.setProjectId(teamMaster.getProjectId());
        ptc.setInUserName(memberId);
        ptc.setInDate(new Date());
        num = projectTmCorpMapper.insertSelective(ptc);
        //添加附件
        if (!teamMaster.getFileAttachmentInfo().isEmpty()) {
            for (FileAttachmentInfo fileAttachmentInfo : teamMaster.getFileAttachmentInfo()) {
                if (StringUtil.isEmpty(fileAttachmentInfo.getBusinessType())) {
                    throw new JxException(ExceptionEnum.File_TYPE_ERROR);
                }
                if (StringUtil.isNotEmpty(fileAttachmentInfo.getUrl())) {
                    fileAttachmentInfo.setUrl(fileAttachmentInfo.getUrl());
                }
                fileAttachmentInfo.setId(new IdWorker().nextId());
                fileAttachmentInfo.setBusinessSysNo(teamMaster.getId());
                fileAttachmentInfo.setInUserName(memberId);
                fileAttachmentInfo.setInDate(new Date());
                fileAttachmentInfo.setIsDel(0);
                num = fileAttachmentInfoMapper.insertSelective(fileAttachmentInfo);
                log.info("添加班组与企业的关系表");
            }
        }
        if (num > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }


    /**
     * * 分页查询班组列表
     * * @param page
     * * @param rows
     * * @param sortBy
     * * @param desc
     * * @param key
     * * @param searchCateId
     * * @return
     */
    public PageResult<TeamMaster> selectTeamMasterByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key, String projectCode) {

        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        try {
            // 开始分页 查询数据库
            PageHelper.startPage(page, rows);
            Map<String, Object> params = new HashMap<>();
            params.put("memberId", memberId);
            if (StringUtils.isNotEmpty(key)) {
                params.put("name", "%" + key + "%");
            }
            if (StringUtils.isNotEmpty(projectCode)) {
                params.put("projectCode", projectCode);
            }
            // 查询
            Page<TeamMaster> pageInfo = teamMasterMapper.selectTeamMasterByPage(params);
            return new PageResult<>(pageInfo.getTotal(), pageInfo);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }

    /**
     * 查询班组详情
     *
     * @param id
     * @return
     */
    public Result selectTeamMasterDetail(String id) {
        //班组信息
        TeamMaster teamMaster = this.teamMasterMapper.selectTeamMasterById(id);
        //班组附件
        List<FileAttachmentInfo> fileAttachmentInfos = fileAttachmentInfoMapper.selectFileAttachmentInfoByBusinessSysNo(id);
        teamMaster.setFileAttachmentInfo(fileAttachmentInfos);
        return Result.success(teamMaster);
    }


    /**
     * 修改班组
     *
     * @param teamMaster
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateTeamMaster(TeamMaster teamMaster) {
        int i = 0;
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        //判断id是否为空 不为空则做修改 为空做添加操作
        if (teamMaster.getId() != null) {
            //判断班组名是否重复
            String id = teamMasterMapper.selectByName(memberId, teamMaster.getTeamName());
            if (StringUtil.isNotEmpty(id) && !teamMaster.getId().equals(id)) {
                return Result.failure("班组名称不能重复");
            }
            saveCorpBasic(teamMaster.getCorpId(), teamMaster.getCorpCode(), teamMaster.getCorpName(), memberId);
            teamMaster.setEditUserName(memberId);
            teamMaster.setEditDate(new Date());
            i = teamMasterMapper.updateByPrimaryKeySelective(teamMaster);
            //修改班组与项目关联
            if (StringUtil.isNotEmpty(teamMaster.getProjectId())) {
                ProjectTmCorp ptc = new ProjectTmCorp();
                ptc.setProjectId(teamMaster.getProjectId());
                Example example = new Example(ProjectCorpInfo.class);
                example.createCriteria().andCondition(" tram_master_id = " + teamMaster.getId());
                projectTmCorpMapper.updateByExampleSelective(ptc, example);
            }

            //添加附件
            if (!teamMaster.getFileAttachmentInfo().isEmpty()) {
                for (FileAttachmentInfo fileAttachmentInfo : teamMaster.getFileAttachmentInfo()) {
                    if (fileAttachmentInfo.getId() != null) {
                        if (StringUtil.isEmpty(fileAttachmentInfo.getBusinessType())) {
                            throw new JxException(ExceptionEnum.File_TYPE_ERROR);
                        }
                        fileAttachmentInfo.setUrl(fileAttachmentInfo.getUrl());
                        fileAttachmentInfo.setId(new IdWorker().nextId());
                        fileAttachmentInfo.setBusinessSysNo(teamMaster.getId());
                        fileAttachmentInfo.setEditUserName(memberId);
                        fileAttachmentInfo.setEditDate(new Date());
                        fileAttachmentInfoMapper.updateByPrimaryKeySelective(fileAttachmentInfo);
                    } else {
                        if (StringUtil.isEmpty(fileAttachmentInfo.getBusinessType())) {
                            throw new JxException(ExceptionEnum.File_TYPE_ERROR);
                        }
                        fileAttachmentInfo.setUrl(fileAttachmentInfo.getUrl());
                        fileAttachmentInfo.setId(new IdWorker().nextId());
                        fileAttachmentInfo.setBusinessSysNo(teamMaster.getId());
                        fileAttachmentInfo.setInUserName(memberId);
                        fileAttachmentInfo.setInDate(new Date());
                        fileAttachmentInfo.setIsDel(0);
                        fileAttachmentInfoMapper.insertSelective(fileAttachmentInfo);
                    }
                }
            }
        } else {
            return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
        }
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 删除班组
     *
     * @param id
     * @return
     */
    public Result updateTeamMasterIsDel(Long id) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("id", id);
        params.put("editDate", new Date());
        int i = teamMasterMapper.updateTeamMasterIsDel(params);
        if (i > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);

    }


    /**
     * 添加项目信息是 判断企业所填企业信息是否存在 若存在 则修改 若不存在 则添加
     */
    private String saveCorpBasic(String id, String corpCode, String corpName, Long memberId) {
        String corpCodeId = null;
        String corpNameId = null;
        CorpBasicInfo cbi = new CorpBasicInfo();
        //判断企业名称是否填写
        if (StringUtils.isNoneEmpty(corpName)) {
            //查询我的企业 是否有相同的企业名称
            corpNameId = this.corpBasicInfoMapper.selectCorpBasicInfoName(memberId, corpName);
        }
        //判断社会统一代码是否填写
        if (StringUtils.isNoneEmpty(corpCode)) {
            //查询我的企业 是否有相同的企业编号
            corpCodeId = this.corpBasicInfoMapper.selectCorpBasicInfoName(memberId, corpCode);
        }
        //如果都没有相同的 添加企业
        if (StringUtil.isEmpty(corpNameId) && StringUtil.isEmpty(corpCodeId)) {
            if (StringUtil.isNotEmpty(corpName)) {
                cbi.setId(new IdWorker().nextId());
                cbi.setCorpName(corpName);
                cbi.setCorpCode(corpCode);
                cbi.setInUserName(memberId);
                cbi.setInDate(new Date());
                corpBasicInfoMapper.installCorpBasicInfo(cbi);
                return cbi.getId().toString();
            }
            return null;
        } else {
            if (StringUtil.isNotEmpty(corpNameId)) {
                cbi.setCorpName(corpName);
                cbi.setCorpCode(corpCode);
                cbi.setEditDate(new Date());
                cbi.setEditUserName(memberId);
                Example example = new Example(CorpBasicCert.class);
                example.createCriteria().andCondition(" id = " + corpNameId + " AND in_user_name = " + memberId);
                corpBasicInfoMapper.updateByExampleSelective(cbi, example);
                return corpNameId;
            } else if (StringUtil.isNotEmpty(corpCodeId)) {
                cbi.setCorpName(corpName);
                cbi.setCorpCode(corpCode);
                cbi.setEditDate(new Date());
                cbi.setEditUserName(memberId);
                Example example = new Example(CorpBasicCert.class);
                example.createCriteria().andCondition(" id = " + corpCodeId + " AND in_user_name = " + memberId);
                corpBasicInfoMapper.updateByExampleSelective(cbi, example);
                return corpCodeId;
            }
            return null;
        }

    }

}
