package com.fantacg.user.mapper;

import com.fantacg.common.dto.worker.ProjectWorkerDto;
import com.fantacg.common.pojo.worker.WorkerInfo;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WorkerInfoMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface WorkerInfoMapper extends Mapper<WorkerInfo> {

    /**
     * 分页查询人员数据表
     * @param params
     * @return
     */
    List<WorkerInfo> selectWorkerInfoByPage(Map<String, Object> params);

    /**
     * 身份证号 身份证姓名 查询身份证信息
     */
    WorkerInfo queryWorkerInfoByCard(@Param(value = "idCardNumber") String idCardNumber, @Param(value = "name") String name);

    /**
     * 身份证信息修改
     */
    int updateWorkerInfoByCard(WorkerInfo workerInfo);

    /**
     * 查询班主是否存在人员信息
     */
    HashMap<String,String> selectProjectWorker(ProjectWorkerDto projectWorkerDto);

    /**
     * 添加到人员信息
     */
    int addProjectWorker(ProjectWorkerDto projectWorkerDto);

    /**
     * 修改工人信息
     * @param projectWorkerDto
     * @return
     */
    int updateProjectWorker(ProjectWorkerDto projectWorkerDto);
}
