package com.heima.model.article.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 文章信息表，存储已发布的文章
 * @TableName ap_article
 */
@TableName(value ="ap_article")
@Data
public class ApArticle implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 文章作者的ID
     */
    @TableField(value = "author_id")
    private Integer authorId;

    /**
     * 作者昵称
     */
    @TableField(value = "author_name")
    private String authorName;

    /**
     * 文章所属频道ID
     */
    @TableField(value = "channel_id")
    private Integer channelId;

    /**
     * 频道名称
     */
    @TableField(value = "channel_name")
    private String channelName;

    /**
     * 文章布局
            0 无图文章
            1 单图文章
            2 多图文章
     */
    @TableField(value = "layout")
    private Integer layout;

    /**
     * 文章标记
            0 普通文章
            1 热点文章
            2 置顶文章
            3 精品文章
            4 大V 文章
     */
    @TableField(value = "flag")
    private Integer flag;

    /**
     * 文章图片
            多张逗号分隔
     */
    @TableField(value = "images")
    private String images;

    /**
     * 文章标签最多3个 逗号分隔
     */
    @TableField(value = "labels")
    private String labels;

    /**
     * 点赞数量
     */
    @TableField(value = "likes")
    private Integer likes;

    /**
     * 收藏数量
     */
    @TableField(value = "collection")
    private Integer collection;

    /**
     * 评论数量
     */
    @TableField(value = "comment")
    private Integer comment;

    /**
     * 阅读数量
     */
    @TableField(value = "views")
    private Integer views;

    /**
     * 省市
     */
    @TableField(value = "province_id")
    private Integer provinceId;

    /**
     * 市区
     */
    @TableField(value = "city_id")
    private Integer cityId;

    /**
     * 区县
     */
    @TableField(value = "county_id")
    private Integer countyId;

    /**
     * 创建时间
     */
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 发布时间
     */
    @TableField(value = "publish_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime publishTime;

    /**
     * 同步状态
     */
    @TableField(value = "sync_status")
    private Integer syncStatus;

    /**
     * 来源
     */
    @TableField(value = "origin")
    private Integer origin;

    /**
     * 
     */
    @TableField(value = "static_url")
    private String staticUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}