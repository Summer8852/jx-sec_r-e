package com.fantacg.project.data.controller;

import com.fantacg.common.pojo.data.SettingData;
import com.fantacg.common.utils.Result;
import com.fantacg.project.service.data.SettingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname SettingDataController
 * @Created by Dupengfei 2020-01-13 13:42
 * @Version 2.0
 */
@RestController
@RequestMapping("/settingData")
public class SettingDataController {


    @Autowired
    SettingDataService settingDataService;

    /**
     * 设置对接账号及平台数据需要传输的数据
     *
     * @param settingDataList
     * @return
     */
    @PostMapping
    public Result settingData(@RequestBody List<SettingData> settingDataList) {
        return settingDataService.addSettingData(settingDataList);
    }

    @PostMapping("/pull")
    public Result settingData(@RequestBody SettingData settingData) {
        return settingDataService.pullSettingData(settingData);
    }

}
