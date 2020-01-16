package com.fantacg.project.service.data.imp;

import com.fantacg.common.enums.Check;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.Result;

import com.fantacg.project.data.vo.ColumnsVo;
import com.fantacg.project.data.vo.TablesVo;
import com.fantacg.project.mapper.DbMapper;
import com.fantacg.project.service.data.DbService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname AnswerLogServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class DbServiceImpl implements DbService {

    @Autowired
    DbMapper dbMapper;

    /**
     * 获取所有表结构(TABLES)
     * @param tableSchema 数据表所属的数据库名
     * @return
     */
    @Override
    public Result tablesALL(String tableSchema,Integer page,Integer rows) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 查询
        Page<TablesVo> pageInfo = (Page<TablesVo>) dbMapper.tablesALL(tableSchema);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 获取表字段(COLUMNS)
     * @param tableSchema 数据表所属的数据库名
     * @param tableName 所属的表名称
     * @return
     */
    @Override
    @Check("tableSchema not null: 数据表所属的数据库名不能为null")
    public Result columns(String tableSchema, String tableName) {
        List<ColumnsVo> columns = dbMapper.columns(tableSchema, tableName);
        return Result.success(columns);
    }
}

