package com.heima.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.search.vo.SearchArticleVo;
import com.heima.model.article.entity.ApArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Oreki
 */
@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    public List<SearchArticleVo> loadArticleList();

}
