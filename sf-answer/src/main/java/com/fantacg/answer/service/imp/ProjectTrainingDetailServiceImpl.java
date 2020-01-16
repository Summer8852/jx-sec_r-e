package com.fantacg.answer.service.imp;

import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.answer.filter.LoginInterceptor;
import com.fantacg.answer.mapper.ProjectTrainingDetailMapper;
import com.fantacg.common.pojo.answer.ProjectTrainingDetail;
import com.fantacg.answer.service.ProjectTrainingDetailService;
import com.fantacg.common.vo.answer.ProjectTrainingDetailVO;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingDetailServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class ProjectTrainingDetailServiceImpl implements ProjectTrainingDetailService {

    @Autowired
    ProjectTrainingDetailMapper detailMapper;

    /**
     * 分页查询培训人在当前账号下的培训详情
     *
     * @param page   分页
     * @param rows   分页
     * @param detail 项目培训基本信息 （AES加密身份证号）
     * @return 返回培训列表
     */
    @Override
    public Result queryDetailsByCardId(Integer page, Integer rows, ProjectTrainingDetail detail) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        // 开始分页
        PageHelper.startPage(page, rows);
        Page<ProjectTrainingDetailVO> pageInfo = this.detailMapper.queryProjectTrainingDetailsByCardId(memberId, AesEncrypt.decryptAES(detail.getIdCardNumber()));
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 查询项目培训详情列表（答题列表）
     *
     * @param dto 项目培训基本信息 （培训id）
     * @return 详情列表
     */
    @Override
    public Result queryDetailsByProjectTrainingId(ProjectTrainingDto dto) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        dto.setMemberId(memberId);
        List<ProjectTrainingDetailVO> projectTrainingDetailVOS = this.detailMapper.queryProjectTrainingDetails(dto);
        return Result.success(projectTrainingDetailVOS);
    }
}
