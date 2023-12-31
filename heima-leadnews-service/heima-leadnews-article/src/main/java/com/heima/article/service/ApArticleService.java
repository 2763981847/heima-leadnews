package com.heima.article.service;

import com.heima.model.article.dto.ArticleDto;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.dto.ArticleInfoDto;
import com.heima.model.article.entity.ApArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.behavior.dto.CollectionBehaviorDto;
import com.heima.model.common.dto.ResponseResult;

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


    ResponseResult<?> saveArticle(ArticleDto dto);

    ResponseResult<?> collection(CollectionBehaviorDto collectionBehaviorDto);

    ResponseResult<?> loadArticleBehavior(ArticleInfoDto articleInfoDto);

    /**
     * 加载文章列表
     *
     * @param dto
     * @param type      1 加载更多   2 加载最新
     * @param firstPage true  是首页  flase 非首页
     * @return
     */
    public List<ApArticle> load2(ArticleHomeDto dto, Short type, boolean firstPage);
}
