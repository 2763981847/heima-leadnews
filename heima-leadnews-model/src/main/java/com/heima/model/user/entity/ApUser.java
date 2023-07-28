package com.heima.model.user.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * APP用户信息表
 *
 * @TableName ap_user
 */
@TableName(value = "ap_user")
@Data
public class ApUser implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 密码、通信等加密盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 用户名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 密码,md5加密
     */
    @TableField(value = "password")
    private String password;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 头像
     */
    @TableField(value = "image")
    private String image;

    /**
     * 0 男
     * 1 女
     * 2 未知
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 0 未
     * 1 是
     */
    @TableField(value = "is_certification")
    private Integer isCertification;

    /**
     * 是否身份认证
     */
    @TableField(value = "is_identity_authentication")
    private Integer isIdentityAuthentication;

    /**
     * 0正常
     * 1锁定
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 0 普通用户
     * 1 自媒体人
     * 2 大V
     */
    @TableField(value = "flag")
    private Integer flag;

    /**
     * 注册时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}