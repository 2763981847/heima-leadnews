package com.heima.article.service;

import com.heima.model.article.entity.ApArticleConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Oreki
 * @description 针对表【ap_article_config(APP已发布文章配置表)】的数据库操作Service
 * @createDate 2023-07-26 10:26:14
 */
public interface ApArticleConfigService extends IService<ApArticleConfig> {
    void updateIsDownListener(String message);
}
