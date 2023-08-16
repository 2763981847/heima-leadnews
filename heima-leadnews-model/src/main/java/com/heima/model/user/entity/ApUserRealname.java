package com.heima.model.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * APP实名认证信息表
 * @TableName ap_user_realname
 */
@TableName(value ="ap_user_realname")
@Data
public class ApUserRealname implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 资源名称
     */
    @TableField(value = "idno")
    private String idno;

    /**
     * 正面照片
     */
    @TableField(value = "font_image")
    private String fontImage;

    /**
     * 背面照片
     */
    @TableField(value = "back_image")
    private String backImage;

    /**
     * 手持照片
     */
    @TableField(value = "hold_image")
    private String holdImage;

    /**
     * 活体照片
     */
    @TableField(value = "live_image")
    private String liveImage;

    /**
     * 状态
            0 创建中
            1 待审核
            2 审核失败
            9 审核通过
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 拒绝原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

    /**
     * 提交时间
     */
    @TableField(value = "submited_time")
    private LocalDateTime submitedTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private LocalDateTime updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}