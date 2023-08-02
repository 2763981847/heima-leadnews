package com.heima.api.article;

import com.heima.api.article.fallback.ArticleClientFallback;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author Oreki
 */
@FeignClient(value = "leadnews-article", path = "/api/v1/article", fallback = ArticleClientFallback.class)
public interface ArticleClient {

    @PostMapping("/save")
    ResponseResult<?> saveArticle(@RequestBody ArticleDto dto);

}