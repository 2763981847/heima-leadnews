package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.entity.ApArticle;
import com.heima.article.service.ApArticleService;
import com.heima.article.mapper.ApArticleMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oreki
 * @description 针对表【ap_article(文章信息表，存储已发布的文章)】的数据库操作Service实现
 * @createDate 2023-07-26 10:26:14
 */
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>
        implements ApArticleService {

    @Override
    public List<ApArticle> load(Short loadType, ArticleHomeDto dto) {
        // 1.参数校验并设置默认值
        if (dto == null) {
            dto = new ArticleHomeDto();
        }
        // 分页大小校验
        Integer size = dto.getSize();
        if (size == null || size < 0 || size > ArticleConstants.MAX_PAGE_SIZE) {
            dto.setSize(ArticleConstants.DEFAULT_PAGE_SIZE);
        }
        // 最大时间校验
        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(LocalDateTime.now());
        }
        // 最小时间校验
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(LocalDateTime.now());
        }
        // tag校验
        String tag = dto.getTag();
        if (tag == null) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        // loadType校验
        if (!ArticleConstants.LOAD_MORE.equals(loadType) &&
                !ArticleConstants.LOAD_NEW.equals(loadType)) {
            loadType = ArticleConstants.LOAD_MORE;
        }
        // 2.根据参数查询文章列表
        List<ApArticle> apArticles = baseMapper.loadArticleList(dto, loadType);
        return apArticles;
    }
}




