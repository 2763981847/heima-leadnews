package com.heima.model.admin.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 管理员用户信息表
 * @author Oreki
 * @TableName ad_user
 */
@TableName(value ="ad_user")
@Data
public class AdUser implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 登录用户名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 登录密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField(value = "image")
    private String image;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 状态
            0 暂时不可用
            1 永久不可用
            9 正常可用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 最后一次登录时间
     */
    @TableField(value = "login_time")
    private LocalDateTime loginTime;

    /**
     * 创建时间
     */
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}