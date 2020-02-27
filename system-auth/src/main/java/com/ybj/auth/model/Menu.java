package com.ybj.auth.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author ybj
 * @since 2020-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_MENU")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 菜单/按钮ID
     */
    @TableId(value = "OID", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 上级菜单ID
     */
    private String parentId;

    /**
     * 菜单/按钮名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * VUE 组件名称  动态加载路由
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    private String type;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 状态：0-正常，1-不可用
     */
    private String status;

    /**
     * 修改时间
     */
    private String creator;

    private String creatorName;

    private LocalDateTime createTime;

    private String updator;

    private String updatorName;

    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    private List<Menu> treeNode;


}
