package com.heima.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.heima.article.service.ApArticleFreemarkerService;
import com.heima.article.service.ApArticleService;
import com.heima.common.config.RabbitConfig;
import com.heima.common.constants.ArticleConstants;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.entity.ApArticleContent;
import com.heima.model.search.vo.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Date;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fu Qiujie
 * @since 2023/7/31
 */
@Service
public class ApArticleFreemarkerServiceImpl implements ApArticleFreemarkerService {

    @Resource
    private Configuration configuration;

    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private ApArticleService apArticleService;

    @Override
    @Async
    public void buildArticleToMinIO(ApArticle apArticle, String content) {
        //1.文章内容通过freemarker生成html文件
        StringWriter out = new StringWriter();
        try {
            Template template = configuration.getTemplate("article.ftl");
            Map<String, Object> params = new HashMap<>();
            params.put("content", JSONArray.parseArray(content));
            template.process(params, out);
        } catch (Exception e) {
            throw new RuntimeException("生成静态文件失败");
        }
        InputStream inputStream = new ByteArrayInputStream(out.toString().getBytes());
        //2.把html文件上传到minio中
        String url = fileStorageService
                .uploadHtmlFile("", apArticle.getId() + ".html", inputStream);
        //3.修改ap_article表，保存static_url字段
        apArticleService.lambdaUpdate().eq(ApArticle::getId, apArticle.getId())
                .set(ApArticle::getStaticUrl, url)
                .update();

        //4.发送消息，创建ES索引
        createArticleESIndex(apArticle, content, url);
    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    private void createArticleESIndex(ApArticle apArticle, String content, String url) {
        SearchArticleVo searchArticleVo = BeanUtil.copyProperties(apArticle, SearchArticleVo.class);
        searchArticleVo.setPublishTime(Date.valueOf(apArticle.getPublishTime().toLocalDate()));
        searchArticleVo.setContent(content);
        searchArticleVo.setStaticUrl(url);
        rabbitTemplate.convertAndSend(
                RabbitConfig.LEADNEWS_TOPIC_EXCHANGE,
                ArticleConstants.ARTICLE_ES_SYNC,
                JSON.toJSONString(searchArticleVo));
    }
}

