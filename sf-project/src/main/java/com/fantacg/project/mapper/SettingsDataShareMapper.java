package com.fantacg.project.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname SettingsDataShareMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface SettingsDataShareMapper {


    /**
     * 查询数据库表
     * @return 表名
     */
    @Select("select * from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    List<Map> findListTable();

    /**
     * 查询数据库字段 @Select("SELECT DISTINCT COLUMN_NAME, COLUMN_COMMENT\n" + "FROM information_schema.COLUMNS WHERE table_name = #{dataTable}")
     *
     * @return 返回字段名
     */
    @Select("SELECT DISTINCT *\n" + "FROM information_schema.COLUMNS WHERE table_name = #{dataTable}")
    List<Map<String, String>> findProjectInfoField(@Param("dataTable") String dataTable);

}
