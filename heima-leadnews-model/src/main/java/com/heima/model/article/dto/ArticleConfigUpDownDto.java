package com.heima.model.article.dto;

import lombok.Data;

/**
 * @author Fu Qiujie
 * @since 2023/8/13
 */
@Data
public class ArticleConfigUpDownDto {
    private Long articleId;
    private Integer isDown;
}
