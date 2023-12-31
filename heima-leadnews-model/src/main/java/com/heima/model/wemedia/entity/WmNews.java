package com.heima.model.wemedia.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 自媒体图文内容信息表
 *
 * @TableName wm_news
 */
@TableName(value = "wm_news")
@Data
public class WmNews implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自媒体用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 图文内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 3 多图文章
     */
    @TableField(value = "type")
    private Short type;

    /**
     * 图文频道ID
     */
    @TableField(value = "channel_id")
    private Integer channelId;

    /**
     *
     */
    @TableField(value = "labels")
    private String labels;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 提交时间
     */
    @TableField(value = "submited_time")
    private LocalDateTime submitedTime;

    /**
     * 当前状态
     * 0 草稿
     * 1 提交（待审核）
     * 2 审核失败
     * 3 人工审核
     * 4 人工审核通过
     * 8 审核通过（待发布）
     * 9 已发布
     */
    @TableField(value = "status")
    private Short status;

    /**
     * 定时发布时间，不定时则为空
     */
    @TableField(value = "publish_time")
    private LocalDateTime publishTime;

    /**
     * 拒绝理由
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 发布库文章ID
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * //图片用逗号分隔
     */
    @TableField(value = "images")
    private String images;

    /**
     *
     */
    @TableField(value = "enable")
    private Integer enable;

    //状态枚举类
    @Alias("WmNewsStatus")
    public enum Status {
        NORMAL((short) 0), SUBMIT((short) 1), FAIL((short) 2), ADMIN_AUTH((short) 3), ADMIN_SUCCESS((short) 4), SUCCESS((short) 8), PUBLISHED((short) 9);
        final Short code;

        Status(short code) {
            this.code = code;
        }

        public Short getCode() {
            return this.code;
        }
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}