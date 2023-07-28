package com.heima.article;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.entity.ApArticleConfig;
import com.heima.model.article.entity.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fu Qiujie
 * @since 2023/7/28
 */
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class ArticleFreemarkerTest {
    @Resource
    private Configuration configuration;

    @Resource
    private FileStorageService fileStorageService;


    @Resource
    private ApArticleService apArticleService;

    @Resource
    private ApArticleContentService apArticleContentService;

    @Test
    public void createStaticUrlTest() throws Exception {
        //1.获取文章内容
        ApArticleContent apArticleContent = apArticleContentService
                .lambdaQuery()
                .eq(ApArticleContent::getArticleId, 1302862387124125698L)
                .one();
        //2.文章内容通过freemarker生成html文件
        StringWriter out = new StringWriter();
        Template template = configuration.getTemplate("article.ftl");
        Map<String, Object> params = new HashMap<>();
        params.put("content", JSONArray.parseArray(apArticleContent.getContent()));
        template.process(params, out);
        InputStream inputStream = new ByteArrayInputStream(out.toString().getBytes());
        //3.把html文件上传到minio中
        String url = fileStorageService
                .uploadHtmlFile("", apArticleContent.getArticleId() + ".html", inputStream);
        //4.修改ap_article表，保存static_url字段
        apArticleService.lambdaUpdate().eq(ApArticle::getId, 1302862387124125698L)
                .set(ApArticle::getStaticUrl, url)
                .update();
    }
}

