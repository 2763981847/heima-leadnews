package com.heima.article.service;

import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.entity.ApArticle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Oreki
 * @description 针对表【ap_article(文章信息表，存储已发布的文章)】的数据库操作Service
 * @createDate 2023-07-26 10:26:14
 */
public interface ApArticleService extends IService<ApArticle> {
    /**
     * 根据参数加载文章列表
     *
     * @param loadType 1为加载更多  2为加载最新
     * @param dto
     * @return
     */
    List<ApArticle> load(Short loadType, ArticleHomeDto dto);
}
