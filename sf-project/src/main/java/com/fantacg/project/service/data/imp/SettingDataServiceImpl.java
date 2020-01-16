package com.fantacg.project.service.data.imp;

import com.fantacg.common.pojo.data.SettingData;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.common.utils.StringUtil;
import com.fantacg.project.mapper.SettingDataMapper;
import com.fantacg.project.service.data.SettingDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname SettingDataService
 * @Created by Dupengfei 2020-01-13 16:10
 * @Version 2.0
 */
@Service
@Slf4j
public class SettingDataServiceImpl implements SettingDataService {

    @Autowired
    SettingDataMapper settingDataMapper;

    /**
     * 设置对接账号及平台数据需要传输的数据
     *
     * @param settingDatas
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addSettingData(List<SettingData> settingDatas) {
        for (SettingData settingData : settingDatas) {
            //判断是否重复  重复则修改  不重复则添加
            Example example = new Example(SettingData.class);
            example.createCriteria()
                    .andEqualTo("accountId", settingData.getAccountId())
                    .andEqualTo("platformId", settingData.getPlatformId())
                    .andEqualTo("tableName", settingData.getTableName());
            SettingData settingData1 = settingDataMapper.selectOneByExample(example);
            if (settingData1 == null) {
                settingData.setId(new IdWorker().nextId());
                settingData.setTableAliasName(StringUtil.camelCaseName(settingData.getTableName().substring(2)));
                settingData.setInDate(new Date());
                settingDataMapper.insertSelective(settingData);
            } else {
                settingData1.setColumnName(settingData.getColumnName());
                settingDataMapper.updateByPrimaryKey(settingData1);
            }
        }
        return Result.success(ResultCode.DATA_ADD_SUCCESS);
    }

    @Override
    public Result pullSettingData(SettingData settingData) {
        List<ConcurrentHashMap<String,Object>> list = new ArrayList<>();
        List<SettingData> settingDatas = settingDataMapper.selectAll();
        for (SettingData data : settingDatas) {
            ConcurrentHashMap<String,Object> concurrentHashMap = new ConcurrentHashMap();
            List<HashMap<String, Object>> lists = settingDataMapper.pullSettingData(data.getTableName(), data.getColumnName());
            concurrentHashMap.put(data.getTableAliasName(),lists);
            list.add(concurrentHashMap);
        }
        return Result.success(list);
    }



}
