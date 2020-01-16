package com.fantacg.project.mapper;

import com.fantacg.common.pojo.data.SettingData;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname SettingDataMapper
 * @Created by Dupengfei 2020-01-13 16:04
 * @Version 2.0
 */
@Repository
public interface SettingDataMapper extends Mapper<SettingData> {

    int addSettingData(List<SettingData> list);

    List<HashMap<String,Object>> pullSettingData(@Param("tableNmae") String tableName, @Param("columnName") String columnName);

}
