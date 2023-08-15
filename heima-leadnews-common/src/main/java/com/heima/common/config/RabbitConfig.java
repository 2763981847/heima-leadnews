package com.heima.common.config;

import com.heima.common.constants.ArticleConstants;
import com.heima.common.constants.WmNewsMessageConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oreki
 */
//Rabbit配置类
@Configuration
public class RabbitConfig {
    public static final String LEADNEWS_TOPIC_EXCHANGE = "leadnews_topic_exchange";


    //创建交换机4
    @Bean
    public Exchange topicExchange() {
        return ExchangeBuilder
                .topicExchange(LEADNEWS_TOPIC_EXCHANGE)//交换机类型 ;参数为名字
                .durable(true)//是否持久化，true即存到磁盘,false只在内存上
                .build();
    }

    //创建队列
    @Bean
    public Queue wmNewsQueue() {
        return QueueBuilder
                .durable(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN)
                .build();
    }

    @Bean
    public Queue articleEsSyncQueue() {
        return QueueBuilder
                .durable(ArticleConstants.ARTICLE_ES_SYNC)
                .build();
    }

    //交换机绑定队列
    @Bean
    public Binding bindMessageQueue() {
        return BindingBuilder
                .bind(wmNewsQueue())
                .to(topicExchange())
                .with(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN)
                .noargs();
    }

    @Bean
    public Binding bindArticleEsQueue() {
        return BindingBuilder
                .bind(articleEsSyncQueue())
                .to(topicExchange())
                .with(ArticleConstants.ARTICLE_ES_SYNC)
                .noargs();
    }
}
