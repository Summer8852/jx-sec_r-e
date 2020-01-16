package com.fantacg.project.service;


import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.dto.project.ProjectWorkerDTO;
import com.fantacg.common.dto.project.ProjectWorkerVO;
import com.fantacg.common.dto.project.PtmVO;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.project.ProjectWorker;
import com.fantacg.common.pojo.project.ProjectWorkerEntryExitHistory;
import com.fantacg.common.pojo.worker.WorkerInfo;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.ProjectWorkerEntryExitHistoryMapper;
import com.fantacg.project.mapper.ProjectWorkerMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname ProjectWorkerService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class ProjectWorkerService {

    @Autowired
    ProjectWorkerMapper projectWorkerMapper;
    @Autowired
    ProjectWorkerEntryExitHistoryMapper projectWorkerEntryExitHistoryMapper;
    @Autowired
    StringRedisTemplate redisTemplate;



    /**
     * 添加项目参建单位-班组-人员数据
     *
     * @param workerInfo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addProjectWorker(WorkerInfo workerInfo) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        workerInfo.setIdCardNumber(AesEncrypt.decryptAES(workerInfo.getIdCardNumber()));

        Example example = new Example(ProjectWorker.class);
        example.createCriteria().andEqualTo("inUserName", memberId);
        example.createCriteria().andEqualTo("idCardNumber", workerInfo.getIdCardNumber());

        int count = projectWorkerMapper.selectCountByExample(example);
        if (count > 0) {
            return Result.failure("该工人已加，请勿重复加入！");
        } else {
            ProjectWorker projectWorker = new ProjectWorker();
            projectWorker.setId(new IdWorker().nextId());
            projectWorker.setProjectCode(workerInfo.getProjectCode());
            projectWorker.setCorpCode(workerInfo.getCorpCode());
            projectWorker.setTeamSysNo(workerInfo.getTeamSysNo());
            projectWorker.setIsTeamLeader(0);
            projectWorker.setWorkerName(workerInfo.getName());
            projectWorker.setIdCardNumber(workerInfo.getIdCardNumber());
            projectWorker.setIdCardType(workerInfo.getIdCardType());
            projectWorker.setWorkType(workerInfo.getWorkerType());
            projectWorker.setWorkerRole(workerInfo.getWorkerRole());
            projectWorker.setInUserName(memberId);
            projectWorker.setInDate(new Date());
            projectWorkerMapper.insertSelective(projectWorker);
            //必须要有班组时 添加项目参建单位-班组-人员-进退场数据表
            if (StringUtil.isNotEmpty(workerInfo.getTeamSysNo())) {
                //添加进场记录
                addProjectWorkerEntryExitHistory(projectWorker, "1");
            }
            //发送redis 消息队列
            redisTemplate.convertAndSend("workerInfo", JSON.toJSONString(workerInfo));
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }

    }

    /**
     * 分页查询工人实名信息列表
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public Result queryProjectWorkerByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        log.info("分页查询工人实名信息列表");
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        //开始分页查询信息
        PageHelper.startPage(page, rows);
        HashMap<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        if (StringUtils.isNotBlank(key)) {
            //判断输入的是姓名 还是身份证号(中文正则表达式：^[\u4e00-\u9fa5]{0,}$)
            boolean result = key.matches("^[\\u4e00-\\u9fa5]{0,}$");
            if (result) {
                params.put("workerName", "%" + key + "%");
            } else {
                params.put("idCardNumber", "%" + key + "%");
            }
        }

        return null;
    }

    /**
     * 筛选班组-人员实名信息列表
     *
     * @param dto
     * @return
     */
    public Result screeningProjectWorker(ProjectWorkerDTO dto) {
        log.info("筛选班组-人员实名信息列表:" + dto);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        dto.setMemberId(memberId);
        //开始分页查询信息
        if (dto.getPage() != null && dto.getRows() != null) {
            PageHelper.startPage(dto.getPage(), dto.getRows());
        }
        String key = dto.getKey();
        if (StringUtils.isNotBlank(key)) {
            //判断输入的是姓名 还是身份证号(中文正则表达式：^[\u4e00-\u9fa5]{0,}$)
            boolean result = key.matches("^[\\u4e00-\\u9fa5]{0,}$");
            if (result) {
                dto.setWorkerName("%" + key + "%");
            } else {
                dto.setIdCardNumber("%" + key + "%");
            }
        }
        Page<ProjectWorkerVO> pageInfo = projectWorkerMapper.screeningProjectWorker(dto);
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 筛选班组-人员实名信息列表(创建项目)
     *
     * @param dto
     * @return
     */
    public Result screeningProjectWorkerByTraining(ProjectWorkerDTO dto) {
        log.info("筛选班组-人员实名信息列表:" + dto);
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        dto.setMemberId(memberId);
        List<PtmVO> list = projectWorkerMapper.screeningProjectWorkerByTraining(dto);
        return Result.success(list);
    }

    /**
     * 批量或单个删除班组人员信息
     *
     * @param lists
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delProjectWorker(List<Long> lists) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        if (!lists.isEmpty()) {
            int i = projectWorkerMapper.delProjectWorker(lists, memberId);
            for (Long list : lists) {
                Example example = new Example(ProjectWorker.class);
                example.createCriteria().andEqualTo("inUserName", memberId).andEqualTo("id", list);
                ProjectWorker projectWorker = this.projectWorkerMapper.selectOneByExample(example);

                if (projectWorker != null) {
                    if (StringUtil.isNotEmpty(projectWorker.getTeamSysNo())) {
                        //添加进场记录
                        addProjectWorkerEntryExitHistory(projectWorker, "0");
                    }
                }
            }
            if (i > 0) {
                return Result.success(ResultCode.DATA_DELETE_SUCCESS);
            }
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }


    /**
     * 分页查询班组人员信息列表
     *
     * @return 返回列表信息
     */
    public Result queryProjectWorkerBySysNoPage(Map map) {
        log.info("分页查询班组人员信息列表");
        Integer page = (Integer) map.get("page");
        Integer rows = (Integer) map.get("rows");
        String teamSysNo = String.valueOf(map.get("teamSysNo"));
        if (page == null || rows == null) {
            page = 1;
            rows = 10;
        }
        if (StringUtil.isNotEmpty(teamSysNo)) {
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            //开始分页查询信息
            PageHelper.startPage(page, rows);
            HashMap<String, Object> params = new HashMap<>();
            params.put("memberId", memberId);
            params.put("teamSysNo", teamSysNo);
            //分页查询结果并返回
            Page<ProjectWorkerVO> pageInfo = projectWorkerMapper.queryProjectWorkerBySysNoPage(params);
            return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
        } else {
            log.info("teamSysNo 班主编号为空");
            return Result.success();
        }
    }


    /**
     * 查询当前班组是否有组长
     */
    public Result queryIsTeamLeader(ProjectWorker projectWorker) {
        ProjectWorker teamLeader = this.isTeamLeader(projectWorker.getTeamSysNo());
        //判断是否存在班组 组长
        //存在 提示某某 为组长
        //不存在 组长不存在 是否将某某设置为组长
        if (null == teamLeader) {
            return Result.success();
        }
        return Result.success();
    }


    /**
     * @param projectWorker
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result isBecomeTeamLeader(ProjectWorker projectWorker) {
        ProjectWorker teamLeader = this.isTeamLeader(projectWorker.getTeamSysNo());
        //存在组长 删除原来的组长
        if (null != teamLeader) {
            this.projectWorkerMapper.updateIsTeamLeader(teamLeader.getTeamSysNo(), teamLeader.getIdCardNumber(), 0);
        }

        //设置成为班组组长
        int i = this.projectWorkerMapper.updateIsTeamLeader(teamLeader.getTeamSysNo(), teamLeader.getIdCardNumber(), 1);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }


    /**
     * 查询 班组是否存在组长
     *
     * @param teamSysNo 班组编号
     * @return 返回班组组长信息
     */
    private ProjectWorker isTeamLeader(String teamSysNo) {
        Example example = new Example(ProjectWorker.class);
        example.createCriteria()
                .andEqualTo("isTeamLeader", 1)
                .andEqualTo("teamSysNo", teamSysNo);
        return this.projectWorkerMapper.selectOneByExample(example);
    }


    private void addProjectWorkerEntryExitHistory(ProjectWorker projectWorker, String type) {
        ProjectWorkerEntryExitHistory projectWorkerEntryExitHistory = new ProjectWorkerEntryExitHistory();
        projectWorkerEntryExitHistory.setId(new IdWorker().nextId());
        projectWorkerEntryExitHistory.setCorpCode(projectWorker.getCorpCode());
        projectWorkerEntryExitHistory.setCorpName(projectWorker.getCorpName());
        projectWorkerEntryExitHistory.setProjectCode(projectWorker.getProjectCode());
        projectWorkerEntryExitHistory.setTeamSysNo(projectWorker.getTeamSysNo());
        projectWorkerEntryExitHistory.setIdCardNumber(projectWorker.getIdCardNumber());
        projectWorkerEntryExitHistory.setIdCardType(String.valueOf(1));
        projectWorkerEntryExitHistory.setDate(new Date());
        projectWorkerEntryExitHistory.setType(type);
        projectWorkerEntryExitHistory.setInUserName(projectWorker.getInUserName());
        projectWorkerEntryExitHistory.setInDate(new Date());
        projectWorkerEntryExitHistoryMapper.insertSelective(projectWorkerEntryExitHistory);
    }
}
