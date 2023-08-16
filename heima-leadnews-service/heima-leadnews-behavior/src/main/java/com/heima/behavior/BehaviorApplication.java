package com.heima.behavior;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
@MapperScan("com.heima.behavior.mapper")
public class BehaviorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BehaviorApplication.class, args);
    }
}
