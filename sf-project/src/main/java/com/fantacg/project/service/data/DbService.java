package com.fantacg.project.service.data;

import com.fantacg.common.utils.Result;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname AnswerLogService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface DbService {

    /**
     * 获取所有表结构(TABLES)
     * @param tableSchema 数据表所属的数据库名
     * @return
     */
    Result tablesALL(String tableSchema, Integer page, Integer rows);

    /**
     * 获取表字段(COLUMNS)
     * @param tableSchema 数据表所属的数据库名
     * @param tableName 所属的表名称
     * @return
     */
    Result columns(String tableSchema, String tableName);

}
