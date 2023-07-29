package com.heima.model.wemedia.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 频道信息表
 *
 * @TableName wm_channel
 */
@TableName(value = "wm_channel")
@Data
public class WmChannel implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 频道描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 是否默认频道
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    /**
     *
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 默认排序
     */
    @TableField(value = "ord")
    private Integer ord;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}