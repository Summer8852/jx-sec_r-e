package com.fantacg.project.service.data;

import com.fantacg.common.pojo.data.SettingData;
import com.fantacg.common.utils.Result;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname SettingDataService
 * @Created by Dupengfei 2020-01-13 16:12
 * @Version 2.0
 */
public interface SettingDataService {

    /**
     * 设置对接账号及平台数据需要传输的数据
     * @param settingDatas
     * @return
     */
    Result addSettingData(List<SettingData> settingDatas);


    /**
     * 数据拉取
     * @param settingData
     * @return
     */
    Result pullSettingData(SettingData settingData);
}
