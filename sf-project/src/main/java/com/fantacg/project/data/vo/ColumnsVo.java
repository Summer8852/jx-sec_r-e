package com.fantacg.project.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname ColumnsVo MySql表字段视图
 * @Created by Dupengfei 2020-01-06 13:45
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnsVo {

    /**
     * 数据表登记目录
     */
    private String tableCatalog;

    /**
     * 数据表所属的数据库名
     */
    private String tableSchema;


    /**
     * 所属的表名称
     */
    private String tableName;


    /**
     * 列名称
     */
    private String columnName;


    /**
     * 字段在表中第几列
     */
    private String  ordinalPosition;

    /**
     * 列的默认数据
     */
    private String columnDefault;

    /**
     * 字段是否可以为空
     */
    private String isNullable;

    /**
     *  数据类型
     */
    private String dataType;

    /**
     * 字符最大长度
     */
    private String characterMaximumLength;

    /**
     * 字节长度？
     */
    private String characterOctetLength;

    /**
     * 数据精度
     */
    private String numericPrecision;

    /**
     * 数据规模
     */
    private String numericScale;

    /**
     * 字符集名称
     */
    private String characterSetName;

    /**
     * 字符集校验名称
     */
    private String collationName;

    /**
     * 列类型
     */
    private String columnType;

    /**
     *  关键列[NULL|MUL|PRI]
     */
    private String columnKey;

    /**
     * 额外描述[NULL|on update CURRENT_TIMESTAMP|auto_increment]
     */
    private String extra;

    /**
     * 字段操作权限[select|select,insert,update,references]
     */
    private String privileges;

    /**
     * 字段注释、描述
     */
    private String columnComment;



}
