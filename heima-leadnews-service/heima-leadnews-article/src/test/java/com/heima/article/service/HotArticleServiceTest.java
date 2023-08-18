package com.heima.article.service;

import com.heima.article.ArticleApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fu Qiujie
 * @since 2023/8/17
 */
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
class HotArticleServiceTest {

    @Resource
    private HotArticleService hotArticleService;

    @Test
    void computeHotArticle() {
        hotArticleService.computeHotArticle();
    }
}