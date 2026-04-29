package com.aicodereview.backend;

import com.aicodereview.backend.config.ClaudeApiProperties;
import com.aicodereview.backend.config.KafkaTopicsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

@SpringBootApplication
@EnableKafkaRetryTopic
@EnableConfigurationProperties({KafkaTopicsProperties.class, ClaudeApiProperties.class})
public class AiCodeReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeReviewApplication.class, args);
    }
}
