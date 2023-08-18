package com.heima.article.mapper;

import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.entity.ApArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oreki
 * @description 针对表【ap_article(文章信息表，存储已发布的文章)】的数据库操作Mapper
 * @createDate 2023-07-26 10:26:14
 * @Entity com.heima.model.article.entity.ApArticle
 */
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    List<ApArticle> findArticleListByLast5days(@Param("dayParam") LocalDateTime dayParam);

    List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDto dto, @Param("type") Short type);
}




