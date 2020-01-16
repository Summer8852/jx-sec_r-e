package com.fantacg.common.pojo.user;

import com.fantacg.common.utils.QpGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Menu 菜单表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_menu")
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID不能为空", groups = {QpGroup.Update.class})
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "父菜单ID不能为空", groups = {QpGroup.Add.class})
    private Long parentId;

    /**
     * 菜单名称
     */
    @Length(min = 2, max = 30, message = "菜单名只能在4~30位之间", groups = {QpGroup.Add.class})
    @NotEmpty(message = "菜单不能为空", groups = {QpGroup.Add.class})
    private String title;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    @NotNull(message = "类型不能为空", groups = {QpGroup.Add.class})
    private Long type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 路径
     */
    private String path;

    /**
     * 是否显示 默认显示在左侧菜单列表（0-默认 1-不显示）
     */
    private Integer isShow;


    /**
     * 首页菜单树需要的结构
     */
    @Transient
    private String action;

    /**
     * 首页菜单树需要的列
     */
    @Transient
    private List<Menu> items;
    /**
     * 角色权限树需要的结构
     */
    @Transient
    private String label;

    /**
     * 角色权限树需要的结构
     */
    @Transient
    private List<Menu> children;


}
