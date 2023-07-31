package com.heima.model.article.dto;

import com.heima.model.article.entity.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Oreki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDto extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}