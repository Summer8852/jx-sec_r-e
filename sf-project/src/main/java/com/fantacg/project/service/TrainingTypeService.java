package com.fantacg.project.service;

import com.fantacg.common.utils.Result;
import com.fantacg.project.mapper.TrainingTypeMapper;
import com.fantacg.common.pojo.project.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname TrainingTypeService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
public class TrainingTypeService {

    @Autowired
    TrainingTypeMapper trainingTypeMapper;

    /**
     * 查询所有培训类型
     *
     * @return
     */
    public Result trainingTypeList() {
        List<TrainingType> trainingTypes = this.trainingTypeMapper.selectAll();
        return Result.success(trainingTypes);
    }

}
