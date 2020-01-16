package com.fantacg.project.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname TablesVo mysql 所有表结构 视图
 * @Created by Dupengfei 2020-01-06 11:28
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TablesVo {

    /**
     * 数据表登记目录
     */
    private String tableCatalog;

    /**
     * 数据表所属的数据库名
     */
    private String tableSchema;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表类型[system view|base table]
     */
    private String tableType;

    /**
     * 使用的数据库引擎[MyISAM|CSV|InnoDB]
     */
    private String engine;

    /**
     *  版本，默认值10
     */
    private String version;

    /**
     *  行格式[Compact|Dynamic|Fixed]
     */
    private String rowFormat;

    /**
     * 表里所存多少行数据
     */
    private String tableRows;

    /**
     * 平均行长度
     */
    private String avgRowLength;

    /**
     * 平均行长度
     */
    private String dataLength;

    /**
     * 最大数据长度
     */
    private String maxDataLength;

    /**
     * 索引长度
     */
    private String indexLength;

    /**
     * 自由数据？
     */
    private String dataFree;

    /**
     * 做自增主键的自动增量当前值
     */
    private String autoIncrement;

    /**
     * 表的创建时间
     */
    private String createTime;

    /**
     *  表的更新时间
     */
    private String  updateTime;

    /**
     * 表的检查时间
     */
    private String checkTime;

    /**
     * 表的字符校验编码集
     */
    private String tableCollation;

    /**
     *  校验和
     */
    private String checksum;

    /**
     * 创建选项
     */
    private String createOptions;

    /**
     * 表的注释、备注
     */
    private String tableComment;



}
