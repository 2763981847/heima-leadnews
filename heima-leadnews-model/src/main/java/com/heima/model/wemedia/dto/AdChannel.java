package com.heima.model.wemedia.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 频道信息表
 *
 * @author Oreki
 * @TableName wm_channel
 */
@TableName(value = "wm_channel")
@Data
public class AdChannel implements Serializable {
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
    private Boolean isDefault;

    /**
     *
     */
    @TableField(value = "status")
    private Boolean status;

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