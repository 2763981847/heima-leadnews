package com.heima.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.vo.HotArticleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fu Qiujie
 * @since 2023/8/17
 */
@Service
public class HotArticleServiceImpl implements HotArticleService {
    @Resource
    private ApArticleMapper apArticleMapper;
    @Resource
    private CacheService cacheService;

    @Override
    public void computeHotArticle() {
        // 查询五天内的所有文章
        LocalDateTime dayParam = LocalDateTime.now().minusDays(5);
        List<ApArticle> articles = apArticleMapper.findArticleListByLast5days(dayParam);
        // 计算文章热度
        List<HotArticleVo> hotArticleVos = computeHotArticle(articles);
        // 取出前三十缓存到redis
        String key = ArticleConstants.HOT_ARTICLE + ArticleConstants.DEFAULT_TAG;
        cacheHotArticle(key, hotArticleVos);
        // 按频道缓存前三十到redis
        hotArticleVos.stream()
                .collect(Collectors.groupingBy(HotArticleVo::getChannelId))
                .forEach((channelId, articleList) -> {
                    String channelKey = ArticleConstants.HOT_ARTICLE + channelId;
                    cacheHotArticle(channelKey, articleList);
                });
    }

    private void cacheHotArticle(String key, List<HotArticleVo> hotArticleVos) {
        List<HotArticleVo> articlesToCache = hotArticleVos.stream()
                .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                .limit(30).collect(Collectors.toList());
        cacheService.set(key, articlesToCache);
    }

    private List<HotArticleVo> computeHotArticle(List<ApArticle> articles) {
        return articles.stream()
                .map(apArticle -> {
                    HotArticleVo hotArticleVo = BeanUtil.copyProperties(apArticle, HotArticleVo.class);
                    hotArticleVo.setScore(computeScore(apArticle));
                    return hotArticleVo;
                }).collect(Collectors.toList());
    }

    private int computeScore(ApArticle apArticle) {
        int score = 0;
        // 1.阅读数
        if (apArticle.getViews() != null) {
            score += apArticle.getViews();
        }
        // 2.点赞数
        if (apArticle.getLikes() != null) {
            score += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        // 3.评论数
        if (apArticle.getComment() != null) {
            score += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }

        // 4.收藏数
        if (apArticle.getCollection() != null) {
            score += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        return score;
    }
}
