package com.heima.article.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WmNewsMessageConstants;
import com.heima.model.article.dto.ArticleConfigUpDownDto;
import com.heima.model.article.entity.ApArticleConfig;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.model.wemedia.dto.WmNewsUpDownDto;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Oreki
 * @description 针对表【ap_article_config(APP已发布文章配置表)】的数据库操作Service实现
 * @createDate 2023-07-26 10:26:14
 */
@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig>
        implements ApArticleConfigService {

    @Override
    @RabbitListener(queues = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN)
    public void updateIsDownListener(String message) {
        if (StrUtil.isBlank(message)) {
            return;
        }
        ArticleConfigUpDownDto articleConfigUpDownDto = JSON.parseObject(message, ArticleConfigUpDownDto.class);
        if (articleConfigUpDownDto == null || articleConfigUpDownDto.getArticleId() == null) {
            return;
        }
        this.lambdaUpdate().eq(ApArticleConfig::getArticleId, articleConfigUpDownDto.getArticleId())
                .set(ApArticleConfig::getIsDown, articleConfigUpDownDto.getIsDown())
                .update();
    }
}




