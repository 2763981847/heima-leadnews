package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.dto.ArticleInfoDto;
import com.heima.model.behavior.dto.CollectionBehaviorDto;
import com.heima.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@RestController
@RequestMapping("/api/v1")
public class ArticleController {

    @Resource
    private ApArticleService apArticleService;

    @PostMapping("/collection_behavior")
    public ResponseResult<?> collection(@RequestBody CollectionBehaviorDto collectionBehaviorDto) {
        return apArticleService.collection(collectionBehaviorDto);
    }


}
