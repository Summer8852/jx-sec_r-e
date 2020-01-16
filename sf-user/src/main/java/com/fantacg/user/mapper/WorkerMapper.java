package com.fantacg.user.mapper;

import com.fantacg.common.pojo.user.Worker;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WorkerMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface WorkerMapper extends Mapper<Worker> {

    /**
     * 实人认证上传资料
     * @param worker
     * @return
     */
    int updateWorkerById(Worker worker);

    /**
     * 手机号查询工人信息
     * @param phone
     * @return
     */
    HashMap<String,Object> workerByPhone(String phone);
}
