package com.fantacg.project.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.common.dto.project.SettingDataDTO;
import com.fantacg.project.service.SettingsDataShareServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname SettingsDataShareController 数据共享设置
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/setting")
public class SettingsDataShareController {

    @Autowired
    SettingsDataShareServer settingsDataShareServer;


    /**
     * 查询数据库表
     *
     * @return 表名
     */
    @PostMapping("/findTables")
    public Result findListTable() {
        return this.settingsDataShareServer.findListTable();
    }

    /**
     * 查询数据库字段
     *
     * @param dataTable 数据库表名
     * @return 单表表信息
     */
    @PostMapping("/findFields/{dataTable}")
    public synchronized Result findFields(@PathVariable("dataTable") String dataTable) {
        return this.settingsDataShareServer.findFields(dataTable);
    }

    /**
     * 根据不同账号 不同设备  不同平台 不同表 设置需要的数据
     */
    @PostMapping("/settingData")
    public Result settingData(@RequestBody SettingDataDTO dto) {

        return null;
    }


}
