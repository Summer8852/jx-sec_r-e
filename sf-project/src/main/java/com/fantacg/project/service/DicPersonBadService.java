package com.fantacg.project.service;

import com.fantacg.common.utils.Result;
import com.fantacg.project.mapper.DicPersonBadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname DicPersonBadService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class DicPersonBadService {
    @Autowired
    DicPersonBadMapper dicPersonBadMapper;

    public Result queryDicPersonBadAll() {
        return Result.success(this.dicPersonBadMapper.selectAll());
    }
}
