package com.fantacg.common.dto.worker;

import com.fantacg.common.pojo.worker.PersonCredential;
import com.fantacg.common.pojo.worker.PersonRegisterInfo;
import com.fantacg.common.pojo.worker.PersonRegisterProfession;
import com.fantacg.common.pojo.worker.WorkerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname WorkerDto
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDto {

    /**
     * 人员实名信息数据
     */
    private WorkerInfo workerInfo;

    /**
     *人员资质数据
     */
    private List<PersonCredential> personCredentials;

    /**
     * 人员注册信息数据
     */
    private List<PersonRegisterInfo> personRegisterInfos;

    /**
     * 人员注册专业数据
     */
    private List<PersonRegisterProfession> personRegisterProfessions;
}
