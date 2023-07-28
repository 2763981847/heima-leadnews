package com.heima.model.article.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Fu Qiujie
 * @since 2023/7/26
 */
@Data
public class ArticleHomeDto {

    // 最大时间
    LocalDateTime maxBehotTime;
    // 最小时间
    LocalDateTime minBehotTime;
    // 分页size
    Integer size;
    // 频道ID
    String tag;
}
