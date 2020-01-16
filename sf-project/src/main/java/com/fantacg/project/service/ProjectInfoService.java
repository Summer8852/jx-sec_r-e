package com.fantacg.project.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.dto.project.ProjectInfos;
import com.fantacg.common.dto.project.ProjectVo;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * * 项目基本信息
 *
 * @author DUPENGFEI
 */
@Service
@Slf4j
public class ProjectInfoService {

    @Autowired
    ProjectInfoMapper projectInfoMapper;
    @Autowired
    ProjectBuilderLicenseMapper projectBuilderLicenseMapper;
    @Autowired
    ProjectCorpInfoMapper projectCorpInfoMapper;
    @Autowired
    TeamMasterMapper teamMasterMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProjectPblPcMapper projectPblPcMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RecycleBinMapper recycleBinMapper;
    @Autowired
    SysDictMapper sysDictMapper;
    @Autowired
    CorpBasicInfoMapper corpBasicInfoMapper;
    @Autowired
    CorpCertInfoManager corpCertInfoManager;
    @Autowired
    CorpBasicCertMapper corpBasicCertMapper;
    @Autowired
    BankCardInfoMapper bankCardInfoMapper;
    @Autowired
    FileAttachmentInfoMapper fileAttachmentInfoMapper;
    @Autowired
    ProjectTmCorpMapper projectTmCorpMapper;
    @Autowired
    SysDictService dictService;

    /**
     * * 分页查询我的项目
     * * @param page
     * * @param rows
     * * @param sortBy
     * * @param desc
     * * @param key
     * * @param searchCateId
     * * @return
     */
    public PageResult<ProjectInfo> queryProjectByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key, String searchCateId) {
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
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            params.put("orderByClause", orderByClause);
        }
        // 查询
        Page<ProjectInfo> pageInfo = (Page<ProjectInfo>) projectInfoMapper.selectAllProjectListByPage(params);
//        if (pageInfo.getTotal() > 0) {
//            // 查询项目分类名或状态
//            for (ProjectInfo projectInfo : pageInfo) {
//                Map<String, Object> map = new HashMap<>();
//                if (StringUtil.isNotEmpty(projectInfo.getCategory())) {
//                    String str = redisTemplate.opsForValue().get(KeyConstant.DICT_NAEM_ID + projectInfo.getCategory());
//                    if (StringUtil.isNotEmpty(str)) {
//                        map = JsonUtils.toMap(str, String.class, Object.class);
//                    } else {
//                        map = sysDictMapper.selectDictById(projectInfo.getCategory());
//                    }
//                    projectInfo.setCategoryName(String.valueOf(map.get("label")));
//                }
//                if (StringUtil.isNotEmpty(projectInfo.getPrjStatus())) {
//                    String str = redisTemplate.opsForValue().get(KeyConstant.DICT_NAEM_ID + projectInfo.getPrjStatus());
//                    if (StringUtil.isNotEmpty(str)) {
//                        map = JsonUtils.toMap(str, String.class, Object.class);
//                    } else {
//                        map = sysDictMapper.selectDictById(projectInfo.getPrjStatus());
//                    }
//                    projectInfo.setStatusName(String.valueOf(map.get("label")));
//                }
//            }
//        }
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    /**
     * * 创建项目
     * * @param projectInfos
     * * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public Result saveProject(ProjectInfos projectInfos) {
        log.info("创建项目:" + projectInfos.toString());
        try {
            int i;
            Long pblId = null;
            List<Long> pcIds = new ArrayList<>();
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

            //项目名必须输入
            log.info("项目名必须输入");
            String name = projectInfos.getProjectInfo().getName();
            if (StringUtils.isEmpty(name)) {
                return Result.failure(ResultCode.PROJECT_NAME_NULL_ERROR);
            }
            //判断项目编码是否唯一
            log.info("判断项目编码是否唯一");
            String code = projectInfos.getProjectInfo().getCode();
            if (StringUtils.isNoneEmpty(code)) {
                int count = projectInfoMapper.selectProjectInfByCode(memberId, code);
                if (count > 0) {
                    return Result.failure(ResultCode.PROJECT_CODE_REPEAT_ERROR);
                }
            }
            //获取项目与企业关联的id
            log.info("获取项目与企业关联的id");
            String contractorCorpId = projectInfos.getProjectInfo().getContractorCorpId();
            String contractorCorpCode = projectInfos.getProjectInfo().getContractorCorpCode();
            String contractorCorpName = projectInfos.getProjectInfo().getContractorCorpName();
            if (StringUtils.isNoneEmpty(contractorCorpName)) {
                contractorCorpId = saveCorpBasic(contractorCorpId, contractorCorpCode, contractorCorpName, memberId);
            }
            //获取项目与建设单位的id
            log.info("获取项目建设单位的id");
            String buildCorpId = projectInfos.getProjectInfo().getBuildCorpId();
            String buildCorpCode = projectInfos.getProjectInfo().getBuildCorpCode();
            String buildCorpName = projectInfos.getProjectInfo().getBuildCorpName();
            if (StringUtils.isNoneEmpty(buildCorpName)) {
                buildCorpId = saveCorpBasic(buildCorpId, buildCorpCode, buildCorpName, memberId);
            }

            log.info("添加 项目基本信息");
            projectInfos.getProjectInfo().setThirdPartyProjectCode(String.valueOf(new IdWorker().nextId()));
            projectInfos.getProjectInfo().setId(new IdWorker().nextId());
            projectInfos.getProjectInfo().setInUserName(memberId);
            projectInfos.getProjectInfo().setInDate(new Date());
            projectInfos.getProjectInfo().setContractorCorpId(contractorCorpId);
            projectInfos.getProjectInfo().setBuildCorpId(buildCorpId);
            projectInfoMapper.insertSelective(projectInfos.getProjectInfo());

            //添加 项目施工许可证
            log.info("添加项目施工许可证");
            if (null != projectInfos.getProjectBuilderLicense()) {
                pblId = this.addOrUpdatePBL(projectInfos.getProjectBuilderLicense(), projectInfos.getProjectInfo().getCode(), memberId);
            }

            //添加 参建单位信息
            if (!projectInfos.getProjectCorpInfos().isEmpty()) {
                pcIds = this.addOrUpdatePCI(projectInfos.getProjectCorpInfos(), projectInfos.getProjectInfo().getCode(), memberId, pcIds);
            }

            //添加项目参建单位-班组数据
            log.info("添加项目参建单位-班组数据");
            if (!projectInfos.getTeamMasters().isEmpty()) {
                for (TeamMaster teamMaster : projectInfos.getTeamMasters()) {
                    //判断班组名是否重复
                    String id = teamMasterMapper.selectByName(memberId, teamMaster.getTeamName());
                    if (StringUtil.isNotEmpty(id)) {
                        return Result.failure("班组名称不能重复");
                    }
                }
                this.addOrUpdateTM(projectInfos.getTeamMasters(), projectInfos.getProjectInfo().getId(), projectInfos.getProjectInfo().getCode(), memberId);
            }

            //添加 项目基本信息 /项目施工许可证 项目参建单位 关联关系
            log.info("添加 项目基本信息 /项目施工许可证 项目参建单位 关联关系");
            ProjectPblPc projectPblPc = new ProjectPblPc();
            projectPblPc.setProjectInfoId(String.valueOf(projectInfos.getProjectInfo().getId()));
            projectPblPc.setPblId(pblId);
            projectPblPc.setPcId(pcIds.toString());
            projectPblPc.setInUserName(memberId);
            projectPblPc.setInDate(new Date());
            i = projectPblPcMapper.insertSelective(projectPblPc);
            if (i > 0) {
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            } else {
                return Result.failure(ResultCode.DATA_ADD_ERROR);
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }

    /**
     * 查询项目详情
     */
    public Result selectProjectDetail(Long id) {
        try {
            log.info("查询项目详情");
            ProjectInfos projectInfos = new ProjectInfos();

            //redis查询项目详情
            String str = redisTemplate.opsForValue().get(KeyConstant.PROJECT_DETAIL + id);
            if (StringUtil.isNotEmpty(str)) {

                projectInfos = objectMapper.readValue(str, ProjectInfos.class);
                log.info("redis查询项目详情:" + projectInfos);
                return Result.success(projectInfos);

            }
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));


            //查询项目/资质/参建单位
            ProjectPblPc projectPblPc = projectPblPcMapper.selectProjectPblPcByProjectId(String.valueOf(id), memberId);

            //查询 项目基本信息
            ProjectInfo projectInfo = projectInfoMapper.selectProjectListById(id);
            projectInfos.setProjectInfo(projectInfo);


            //查询 施工许可证
            if (projectPblPc != null) {
                ProjectBuilderLicense projectBuilderLicense = projectBuilderLicenseMapper.selectProjectBuilderLicense(projectPblPc.getPblId());
                projectInfos.setProjectBuilderLicense(projectBuilderLicense);
            }
            //查询 项目参建单位信息/项目参建单位信息
            List<Long> pcids = objectMapper.readValue(projectPblPc.getPcId(), List.class);
            List<ProjectCorpInfo> projectCorpInfos = new ArrayList<>();
            if (!pcids.isEmpty()) {
                projectCorpInfos = projectCorpInfoMapper.selectProjectCorpInfoByList(pcids);
            }
            projectInfos.setProjectCorpInfos(projectCorpInfos);

            //查询班组 列表
            List<TeamMaster> teamMasters = teamMasterMapper.selectTeamMasterByProjectId(String.valueOf(id), memberId);
            if (!teamMasters.isEmpty()) {
                for (TeamMaster teamMaster : teamMasters) {
                    List<FileAttachmentInfo> fileAttachmentInfos = fileAttachmentInfoMapper.selectFileAttachmentInfoByBusinessSysNo(teamMaster.getId());
                    teamMaster.setFileAttachmentInfo(fileAttachmentInfos);
                }
            }
            projectInfos.setTeamMasters(teamMasters);
            redisTemplate.opsForValue().set(KeyConstant.PROJECT_DETAIL + id, objectMapper.writeValueAsString(projectInfos));
            return Result.success(projectInfos);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * * 删除项目
     * * @param id
     * * @return
     */
    public Result delProjectInfo(Long id) {
        log.info("删除项目");
        try {
            redisTemplate.delete(KeyConstant.PROJECT_DETAIL + id);
            //获取用户id
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

            RecycleBin recycleBin = new RecycleBin();
            recycleBin.setRemoveId(id);
            recycleBin.setRemoveType(2);
            //防止重复删除
            int count = recycleBinMapper.selectCount(recycleBin);
            if (count > 0) {
                return Result.failure(ResultCode.DATA_DELETE_ERROR);
            }

            int i = 0;
            //修改状态
            Example example = new Example(ProjectInfo.class);
            example.createCriteria().andCondition(" id = " + id + " AND in_user_name = " + memberId);
            ProjectInfo projectInfo = new ProjectInfo();
            //项目状态 000 表示已删除
            projectInfo.setPrjStatus("000");
            projectInfo.setEditUserName(memberId);
            projectInfo.setEditDate(new Date());
            i = projectInfoMapper.updateByExampleSelective(projectInfo, example);

            if (i > 0) {
                recycleBin.setInDate(new Date());
                recycleBin.setMemberId(memberId);
                //今天的时间加30天（将保留的数据存储30天）
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(recycleBin.getInDate());
                calendar.add(Calendar.DAY_OF_MONTH, +30);
                recycleBin.setRemoveDate(calendar.getTime());
                //添加删除记录
                recycleBinMapper.insertSelective(recycleBin);
                return Result.success(ResultCode.DATA_DELETE_SUCCESS);
            }

            return Result.failure(ResultCode.DATA_DELETE_ERROR);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }

    /**
     * * 修改项目
     * * @param projectInfos
     * * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result editProjectInfo(ProjectInfos projectInfos) {
        log.info("修改项目:" + projectInfos);
        try {
            redisTemplate.delete(KeyConstant.PROJECT_DETAIL + projectInfos.getProjectInfo().getId());
            int i = 0;
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

            //项目名必须输入
            String name = projectInfos.getProjectInfo().getName();
            if (StringUtils.isEmpty(name)) {
                return Result.failure(ResultCode.PROJECT_NAME_NULL_ERROR);
            }

            //判断项目编码是否唯一
            String code = projectInfos.getProjectInfo().getCode();
            if (StringUtils.isNoneEmpty(code)) {
                String s = projectInfoMapper.selectProjectCodeById(projectInfos.getProjectInfo().getId());
                //判断修改的项目编码是都与原项目编码相同 如果不相同 判断code 是否重复
                if (!code.equals(s)) {
                    int count = projectInfoMapper.selectProjectInfByCode(memberId, code);
                    if (count > 0) {
                        return Result.failure(ResultCode.PROJECT_CODE_REPEAT_ERROR);
                    }
                }
            }

            //总承包单位信息
            String contractorCorpId = projectInfos.getProjectInfo().getContractorCorpId();
            String contractorCorpCode = projectInfos.getProjectInfo().getContractorCorpCode();
            String contractorCorpName = projectInfos.getProjectInfo().getContractorCorpName();
            if (StringUtils.isNoneEmpty(contractorCorpName)) {
                contractorCorpId = saveCorpBasic(contractorCorpId, contractorCorpCode, contractorCorpName, memberId);
            }

            //建设单位信息
            String buildCorpId = projectInfos.getProjectInfo().getBuildCorpId();
            String buildCorpCode = projectInfos.getProjectInfo().getBuildCorpCode();
            String buildCorpName = projectInfos.getProjectInfo().getBuildCorpName();
            if (StringUtils.isNoneEmpty(buildCorpName)) {
                buildCorpId = saveCorpBasic(buildCorpId, buildCorpCode, buildCorpName, memberId);
            }

            //修改 项目基本信息
            log.info("修改 项目基本信息:" + projectInfos.getProjectInfo());
            projectInfos.getProjectInfo().setContractorCorpId(contractorCorpId);
            projectInfos.getProjectInfo().setBuildCorpId(buildCorpId);
            projectInfos.getProjectInfo().setEditUserName(memberId);
            projectInfos.getProjectInfo().setEditDate(new Date());
            Example example = new Example(ProjectInfo.class);
            example.createCriteria().andCondition(" id = " + projectInfos.getProjectInfo().getId() + " AND in_user_name = " + memberId);
            projectInfoMapper.updateByExampleSelective(projectInfos.getProjectInfo(), example);

            //查询 项目 施工许可证
            ProjectPblPc ProjectPblPc = projectPblPcMapper.selectProjectPblPcByProjectId(String.valueOf(projectInfos.getProjectInfo().getId()), memberId);
            //项目施工许可证数据表 id
            Long pblId = ProjectPblPc.getPblId();
            //项目参建单位信息数据表 id
            List<Long> pcId = objectMapper.readValue(ProjectPblPc.getPcId(), List.class);

            //添加或修改 项目施工许可证
            if (StringUtil.isNotEmpty(projectInfos.getProjectBuilderLicense().getPrjName()) || StringUtil.isNotEmpty(projectInfos.getProjectBuilderLicense().getBuilderLicenseNum())) {
                log.info("添加或修改 项目施工许可证");
                pblId = this.addOrUpdatePBL(projectInfos.getProjectBuilderLicense(), projectInfos.getProjectInfo().getCode(), memberId);
            }

            //添加或修改 项目参建单位信息
            if (!projectInfos.getProjectCorpInfos().isEmpty()) {
                log.info("添加或修改 项目参建单位信息");
                pcId = this.addOrUpdatePCI(projectInfos.getProjectCorpInfos(), projectInfos.getProjectInfo().getCode(), memberId, pcId);
            }


            //添加 项目参建单位-班组
            if (!projectInfos.getTeamMasters().isEmpty()) {
                //判断班组名称是否重复
                for (TeamMaster teamMaster : projectInfos.getTeamMasters()) {
                    String id = teamMasterMapper.selectByName(memberId, teamMaster.getTeamName());
                    if (StringUtil.isEmpty(teamMaster.getId())) {
                        if (StringUtil.isNotEmpty(id)) {
                            return Result.failure("班组名称不能重复");
                        }
                    } else {
                        if (StringUtil.isNotEmpty(id) && !teamMaster.getId().equals(id)) {
                            return Result.failure("班组名称不能重复");
                        }
                    }

                }
                log.info("添加 项目参建单位-班组");
                this.addOrUpdateTM(projectInfos.getTeamMasters(), projectInfos.getProjectInfo().getId(), projectInfos.getProjectInfo().getCode(), memberId);
            }

            //项目施工许可证数据表 id
            ProjectPblPc.setPblId(pblId);
            //项目参建单位信息数据表 id
            ProjectPblPc.setPcId(pcId.toString());
            ProjectPblPc.setEditUserName(memberId);
            ProjectPblPc.setEditDate(new Date());
            Example example4 = new Example(ProjectPblPc.class);
            example4.createCriteria().andCondition(" id = " + ProjectPblPc.getId() + " AND in_user_name = " + memberId);
            i = projectPblPcMapper.updateByExampleSelective(ProjectPblPc, example4);

            if (i > 0) {
                return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
            }
            return Result.success(ResultCode.DATA_UPDATE_ERROR);

        } catch (Exception e) {
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }

    /**
     * * 创建二维码 权限缓存1天
     * * @param code
     */
    public void generatorQrCode(String code) {
        log.info("创建二维码 权限缓存1天");
        this.redisTemplate.opsForValue().set(KeyConstant.PROJECT_KEY_PREFIX + code, "1", 1, TimeUnit.DAYS);
    }

    /**
     * * 添加项目信息是 判断企业所填企业信息是否存在 若存在 则修改 若不存在 则添加
     */
    private String saveCorpBasic(String id, String corpCode, String corpName, Long memberId) {
        log.info("添加项目信息是 判断企业所填企业信息是否存在 若存在 则修改 若不存在 则添加:" + id + "," + corpCode + "," + corpName + "," + memberId);
        String corpCodeId = null;
        String corpNameId = null;
        CorpBasicInfo cbi = new CorpBasicInfo();
        //判断企业名称是否填写
        if (StringUtils.isNotEmpty(corpName)) {
            //查询我的企业 是否有相同的企业名称
            corpNameId = this.corpBasicInfoMapper.selectCorpBasicInfoName(memberId, corpName);
        }
        //判断社会统一代码是否填写
        if (StringUtils.isNotEmpty(corpCode)) {
            //查询我的企业 是否有相同的企业编号
            corpCodeId = this.corpBasicInfoMapper.selectCorpBasicInfoCode(memberId, corpCode);
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
            return id;
        } else {
            if (StringUtil.isNotEmpty(corpNameId)) {
                if (StringUtil.isNotEmpty(corpName)) {
                    cbi.setCorpName(corpName);
                    cbi.setCorpCode(corpCode);
                    cbi.setEditDate(new Date());
                    cbi.setEditUserName(memberId);
                    Example example = new Example(CorpBasicCert.class);
                    example.createCriteria().andCondition(" id = " + corpNameId + " AND in_user_name = " + memberId);
                    corpBasicInfoMapper.updateByExampleSelective(cbi, example);
                    return corpNameId;
                }
                return id;
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
            return id;
        }

    }


    /**
     * * 查询项目编号和项目名称
     * * @return
     */
    public Result getProjectCode() {
        log.info("查询项目编号和项目名称");
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        List<ProjectVo> projectVos = projectInfoMapper.selectProjectCodeAll(memberId);
        return Result.success(projectVos);
    }

    /**
     * 删除项目施工许可证数
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deletePBL(String pId, Long pblId) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        int count = 0;
        //删除资质
        ProjectBuilderLicense pbl = new ProjectBuilderLicense();
        pbl.setId(pblId);
        pbl.setInUserName(memberId);
        projectBuilderLicenseMapper.delete(pbl);

        ProjectPblPc projectPblPc = new ProjectPblPc();
        projectPblPc.setEditDate(new Date());
        projectPblPc.setEditUserName(memberId);
        projectPblPc.setPblId(0L);
        Example example1 = new Example(ProjectBuilderLicense.class);
        example1.createCriteria().andCondition(" project_info_id = " + pId + " AND in_user_name = " + memberId);
        count = projectPblPcMapper.updateByExampleSelective(projectPblPc, example1);
        if (count > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

    /**
     * 删除项目参建单位信息数
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deletePC(String pId, Long pblId) {
        if (pblId != null) {
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            int count = 0;
            //删除项目参建单位信息数
            ProjectCorpInfo pci = new ProjectCorpInfo();
            pci.setId(pblId);
            pci.setInUserName(memberId);
            projectCorpInfoMapper.delete(pci);

            //项目参建单位信息数据表 id
            ProjectPblPc ProjectPblPc = projectPblPcMapper.selectProjectPblPcByProjectId(String.valueOf(pId), memberId);
            List<Long> pcId =  JSON.parseObject(ProjectPblPc.getPcId(),List.class);
            Iterator<Long> it = pcId.iterator();
            while (it.hasNext()) {
                Long x = it.next();
                if (x.equals(pblId)) {
                    it.remove();
                }
            }

            ProjectPblPc projectPblPc = new ProjectPblPc();
            projectPblPc.setEditDate(new Date());
            projectPblPc.setEditUserName(memberId);
            projectPblPc.setPcId(String.valueOf(pcId));
            Example example1 = new Example(ProjectBuilderLicense.class);
            example1.createCriteria().andCondition(" project_info_id = " + pId + " AND in_user_name = " + memberId);
            count = projectPblPcMapper.updateByExampleSelective(projectPblPc, example1);
            if (count > 0) {
                return Result.success(ResultCode.DATA_DELETE_SUCCESS);
            }
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result deleteTM(String pId, String tmId) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        ProjectTmCorp projectTmCorp = new ProjectTmCorp();
        projectTmCorp.setTramMasterId(tmId);
        projectTmCorp.setProjectId(pId);
        projectTmCorp.setInUserName(memberId);
        int delete = projectTmCorpMapper.delete(projectTmCorp);
        if (delete > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

    /**
     * 添加或修改项目施工许可证
     */
    private Long addOrUpdatePBL(ProjectBuilderLicense pbl, String projectCode, Long memberId) {
        log.info("pbl:" + pbl.toString());
        //判断id是否为空 为空做添加操作
        if (pbl.getId() != null && pbl.getId() != 0) {
            pbl.setProjectCode(projectCode);
            pbl.setEditUserName(memberId);
            pbl.setEditDate(new Date());
            Example example1 = new Example(ProjectBuilderLicense.class);
            example1.createCriteria().andCondition(" id = " + pbl.getId() + " AND in_user_name = " + memberId);
            projectBuilderLicenseMapper.updateByExampleSelective(pbl, example1);
        } else {
            pbl.setProjectCode(projectCode);
            pbl.setInUserName(memberId);
            pbl.setInDate(new Date());
            projectBuilderLicenseMapper.insertSelective(pbl);
        }
        return pbl.getId();
    }

    /**
     * 添加或修改 项目参建单位信息
     */
    private List<Long> addOrUpdatePCI(List<ProjectCorpInfo> projectCorpInfos, String projectCode, Long memberId, List<Long> pcIds) {
        for (ProjectCorpInfo projectCorpInfo : projectCorpInfos) {
            String corpId = saveCorpBasic(projectCorpInfo.getCorpId(), projectCorpInfo.getCorpCode(), projectCorpInfo.getCorpName(), memberId);
            if (projectCorpInfo.getId() != null && projectCorpInfo.getId() != 0) {
                Example example = new Example(ProjectCorpInfo.class);

                log.info("修改项目参建单位信息：" + projectCorpInfo);
                projectCorpInfo.setEditUserName(memberId);
                projectCorpInfo.setEditDate(new Date());
                projectCorpInfo.setProjectCode(projectCode);
                example.createCriteria().andCondition(" id = " + projectCorpInfo.getId() + " AND in_user_name = " + memberId);
                projectCorpInfoMapper.updateByExampleSelective(projectCorpInfo, example);
                if (StringUtil.isNotEmpty(projectCorpInfo.getBankCardInfo())) {
                    log.info("修改参建单位的银行卡：" + projectCorpInfo);
                    BankCardInfo bankCardInfo = new BankCardInfo();
                    bankCardInfo.setBankLinkNumber(projectCorpInfo.getBankLinkNumber());
                    bankCardInfo.setBankName(projectCorpInfo.getBankName());
                    bankCardInfo.setBankNumber(projectCorpInfo.getBankNumber());
                    bankCardInfo.setBusinessSysNo(projectCorpInfo.getBusinessSysNo());
                    bankCardInfo.setBusinessType(projectCorpInfo.getBusinessType());
                    bankCardInfo.setEditUserName(memberId);
                    bankCardInfo.setEditDate(new Date());
                    example = new Example(BankCardInfo.class);
                    example.createCriteria().andCondition(" id = " + projectCorpInfo.getBankCardInfo() + " AND in_user_name = " + memberId);
                    bankCardInfoMapper.updateByExampleSelective(bankCardInfo, example);
                }
            } else {
                //添加参建单位的银行卡
                log.info("添加参建单位的银行卡");
                BankCardInfo b = new BankCardInfo();
                b.setId(String.valueOf(new IdWorker().nextId()));
                b.setBankLinkNumber(projectCorpInfo.getBankLinkNumber());
                b.setBankName(projectCorpInfo.getBankName());
                b.setBankNumber(projectCorpInfo.getBankNumber());
                b.setBusinessSysNo(projectCorpInfo.getBusinessSysNo());
                b.setBusinessType(projectCorpInfo.getBusinessType());
                b.setInUserName(memberId);
                b.setInDate(new Date());
                bankCardInfoMapper.insertSelective(b);

                //添加项目参建单位信息
                log.info("添加项目参建单位信息");
                projectCorpInfo.setBankCardInfo(b.getId());
                projectCorpInfo.setCorpId(corpId);
                projectCorpInfo.setInUserName(memberId);
                projectCorpInfo.setInDate(new Date());
                projectCorpInfo.setProjectCode(projectCode);
                projectCorpInfoMapper.installProjectCorpInfo(projectCorpInfo);
                pcIds.add(projectCorpInfo.getId());
            }
        }
        return pcIds;
    }

    /**
     * 添加或修改 项目参建单位-班组
     */
    private void addOrUpdateTM(List<TeamMaster> teamMasters, Long projectId, String projectCode, Long memberId) {
        for (TeamMaster teamMaster : teamMasters) {
            String corpId = teamMaster.getCorpId();
            corpId = saveCorpBasic(corpId, teamMaster.getCorpCode(), teamMaster.getCorpName(), memberId);
            if (StringUtil.isNotEmpty(teamMaster.getId())) {
                //修改班组基本信息
                teamMaster.setEditUserName(memberId);
                teamMaster.setEditDate(new Date());
                teamMaster.setProjectCode(projectCode);
                Example example3 = new Example(ProjectCorpInfo.class);
                example3.createCriteria().andCondition(" id = " + teamMaster.getId() + " AND in_user_name = " + memberId);
                teamMasterMapper.updateByExampleSelective(teamMaster, example3);

                //添加或修改班组附件
                if (!teamMaster.getFileAttachmentInfo().isEmpty()) {
                    for (FileAttachmentInfo fileAttachmentInfo : teamMaster.getFileAttachmentInfo()) {
                        if (fileAttachmentInfo.getId() != null) {
                            if (StringUtil.isEmpty(fileAttachmentInfo.getBusinessType())) {
                                throw new JxException(ExceptionEnum.File_TYPE_ERROR);
                            }
                            if (StringUtil.isNotEmpty(fileAttachmentInfo.getUrl())) {
                                fileAttachmentInfo.setUrl(fileAttachmentInfo.getUrl());
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
                            if (StringUtil.isNotEmpty(fileAttachmentInfo.getUrl())) {
                                fileAttachmentInfo.setUrl(fileAttachmentInfo.getUrl());
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
                //添加班组信息
                log.info("添加班组信息" + teamMasters);
                teamMaster.setId(String.valueOf(new IdWorker().nextId()));
                teamMaster.setTeamSysNo(this.teamSysNo());
                teamMaster.setInUserName(memberId);
                teamMaster.setInDate(new Date());
                teamMaster.setCorpId(corpId);
                teamMaster.setProjectCode(projectCode);
                teamMasterMapper.installTeamMasters(teamMaster);

                //添加班组与企业的关系表
                log.info("添加班组与企业的关系表");
                ProjectTmCorp ptc = new ProjectTmCorp();
                ptc.setTramMasterId(teamMaster.getId());
                ptc.setCorpBasicId(corpId);
                ptc.setProjectId(String.valueOf(projectId));
                ptc.setInUserName(memberId);
                ptc.setInDate(new Date());
                projectTmCorpMapper.insertSelective(ptc);

                //添加附件
                if (!teamMaster.getFileAttachmentInfo().isEmpty()) {
                    log.info("添加班组与企业的关系表");
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
                        fileAttachmentInfo.setBusinessType("003");
                        fileAttachmentInfo.setInDate(new Date());
                        fileAttachmentInfo.setIsDel(0);
                        fileAttachmentInfoMapper.insertSelective(fileAttachmentInfo);
                    }
                }
            }
        }
    }

    private String teamSysNo() {
        //使用NO- +  时间格式化yyyyMMdd + redis自增
        RedisAtomicLong entityIdCounter = new RedisAtomicLong("teamSysNo", redisTemplate.getConnectionFactory());
        Long teamSysNo = entityIdCounter.getAndIncrement();
        String formatTime = DateUtil.format(new Date(), "yyyy/MM/dd");
        log.info("班组编号 ：" + "NO-" + formatTime + teamSysNo);
        StringBuffer stringBuffer = new StringBuffer();
        return "NO-" + formatTime + teamSysNo;
    }


}
