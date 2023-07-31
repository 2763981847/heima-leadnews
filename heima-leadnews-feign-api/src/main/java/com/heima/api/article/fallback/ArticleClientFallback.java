package com.heima.api.article.fallback;

import com.heima.api.article.ArticleClient;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * @author Fu Qiujie
 * @since 2023/7/31
 */
@Component
public class ArticleClientFallback implements ArticleClient {
    @Override
    public ResponseResult<?> saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
