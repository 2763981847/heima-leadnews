package com.heima.article.service;

import com.heima.model.article.entity.ApArticle;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author Fu Qiujie
 * @since 2023/7/31
 */
public interface ApArticleFreemarkerService {


    /**
     * 生成静态文件上传到minIO中
     *
     * @param apArticle
     * @param content
     */
    void buildArticleToMinIO(ApArticle apArticle, String content);
}
