package com.fantacg.answer.service;

import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.pojo.answer.ProjectTrainingDetail;
import com.fantacg.common.utils.Result;
import org.springframework.stereotype.Service;

/**
 * 培训详情
 *
 * @author DUPENGFEI
 */


@Service
public interface ProjectTrainingDetailService {

    /**
     * 分页查询培训人在当前账号下的培训详情
     *
     * @param page   分页
     * @param rows   分页
     * @param detail 项目培训基本信息 （AES加密身份证号）
     * @return 返回培训列表
     */
    Result queryDetailsByCardId(Integer page, Integer rows, ProjectTrainingDetail detail);

    /**
     * 查询项目培训详情列表（答题列表）
     *
     * @param dto 项目培训基本信息 （培训id）
     * @return 详情列表
     */
    Result queryDetailsByProjectTrainingId(ProjectTrainingDto dto);

}
