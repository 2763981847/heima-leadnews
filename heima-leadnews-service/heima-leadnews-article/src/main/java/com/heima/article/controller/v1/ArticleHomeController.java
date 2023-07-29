package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Fu Qiujie
 * @since 2023/7/26
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    @Resource
    private ApArticleService apArticleService;

    @PostMapping("/load")
    public ResponseResult<?> load(@RequestBody ArticleHomeDto dto) {
        List<ApArticle> apArticles = apArticleService.load(ArticleConstants.LOAD_MORE, dto);
        return ResponseResult.okResult(apArticles);
    }

    @PostMapping("/loadmore")
    public ResponseResult<?> loadMore(@RequestBody ArticleHomeDto dto) {
        List<ApArticle> apArticles = apArticleService.load(ArticleConstants.LOAD_MORE, dto);
        return ResponseResult.okResult(apArticles);
    }

    @PostMapping("/loadnew")
    public ResponseResult<?> loadNew(@RequestBody ArticleHomeDto dto) {
        List<ApArticle> apArticles = apArticleService.load(ArticleConstants.LOAD_NEW, dto);
        return ResponseResult.okResult(apArticles);
    }
}