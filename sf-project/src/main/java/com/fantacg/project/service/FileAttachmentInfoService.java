package com.fantacg.project.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.utils.IdGenerator;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.FileAttachmentInfoMapper;
import com.fantacg.common.pojo.project.CorpCertInfo;
import com.fantacg.common.pojo.project.FileAttachmentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname FileAttachmentInfoService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
@Service
public class FileAttachmentInfoService {

    @Autowired
    FileAttachmentInfoMapper fileAttachmentInfoMapper;


    /**
     * 添加附件
     *
     * @param info
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addFileAttachmentInfo(FileAttachmentInfo info) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        info.setId(new IdGenerator().snowflakeId());
        info.setInUserName(memberId);
        info.setInDate(new Date());
        info.setIsDel(0);
        int i = this.fileAttachmentInfoMapper.insertSelective(info);
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deleteFileAttachmentInfo(Long id) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        FileAttachmentInfo info = new FileAttachmentInfo();
        info.setIsDel(1);
        Example example = new Example(CorpCertInfo.class);
        example.createCriteria().andCondition("id = " + id + " AND in_user_name = " + memberId);
        int i = this.fileAttachmentInfoMapper.updateByExampleSelective(info, example);
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }
}
