package com.fantacg.project.mapper;

import com.fantacg.project.data.vo.ColumnsVo;
import com.fantacg.project.data.vo.TablesVo;
import com.github.pagehelper.Page;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname DbMapper
 * @Created by Dupengfei 2020-01-06 11:05
 * @Version 2.0
 */
@Repository
public interface DbMapper {

    /**
     * 获取所有表结构(TABLES)
     * @return
     */
   Page<TablesVo> tablesALL(@Param("tableSchema") String tableSchema);

    /**
     * 获取表字段(COLUMNS)
     * @param tableName
     * @return
     */
   List<ColumnsVo> columns(
           @Param("tableSchema") String tableSchema,
           @Param("tableName") String tableName
   );

}
