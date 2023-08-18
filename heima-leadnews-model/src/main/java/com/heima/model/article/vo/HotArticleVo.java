package com.heima.model.article.vo;


import com.heima.model.article.entity.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Oreki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HotArticleVo extends ApArticle {
    /**
     * 文章分值
     */
    private Integer score;
}