package com.aicodereview.backend.messaging;

import com.aicodereview.backend.config.KafkaTopicsProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReviewRequestProducer {

    private final KafkaTemplate<String, ReviewRequestedEvent> kafkaTemplate;
    private final KafkaTopicsProperties topicsProperties;

    public ReviewRequestProducer(
            KafkaTemplate<String, ReviewRequestedEvent> kafkaTemplate,
            KafkaTopicsProperties topicsProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicsProperties = topicsProperties;
    }

    public void publish(ReviewRequestedEvent event) {
        kafkaTemplate.send(topicsProperties.reviewRequests(), event.reviewId().toString(), event);
    }
}
