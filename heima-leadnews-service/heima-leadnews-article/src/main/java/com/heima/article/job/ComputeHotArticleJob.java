package com.heima.article.job;

import com.heima.article.service.HotArticleService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Oreki
 */
@Component
public class ComputeHotArticleJob {

    @Resource
    private HotArticleService hotArticleService;

    @XxlJob("computeHotArticleJob")
    public void handle() {
        hotArticleService.computeHotArticle();
    }
}