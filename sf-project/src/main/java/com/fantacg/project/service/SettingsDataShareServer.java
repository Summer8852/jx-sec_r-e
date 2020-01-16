package com.fantacg.project.service;


import com.fantacg.common.utils.Result;
import com.fantacg.project.mapper.SettingsDataShareMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname SettingsDataShareServer
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class SettingsDataShareServer {

    @Autowired
    SettingsDataShareMapper settingsDataShareMapper;

    /**
     * 查询数据库表
     * @return 表名
     */
    public Result findListTable() {
        return Result.success(this.settingsDataShareMapper.findListTable());
    }
    /**
     * 查询数据库字段
     *
     * @param dataTable 数据库表名
     * @return 返回单表表信息
     */
    public Result findFields(String dataTable) {
        return Result.success(this.settingsDataShareMapper.findProjectInfoField(dataTable));
    }
}
