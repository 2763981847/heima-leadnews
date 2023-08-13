package com.heima.wemedia;

import com.heima.common.constants.WmNewsMessageConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author Fu Qiujie
 * @since 2023/8/13
 */
@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class KafkaTest {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend() throws ExecutionException, InterruptedException {
        rabbitTemplate.convertAndSend("leadnews_topic_exchange", WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN, "1");
    }

}
