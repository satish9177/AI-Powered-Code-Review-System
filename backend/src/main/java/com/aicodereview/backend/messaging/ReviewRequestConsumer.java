package com.aicodereview.backend.messaging;

import com.aicodereview.backend.service.ReviewProcessingService;
import java.util.UUID;
import org.springframework.kafka.annotation.Backoff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ReviewRequestConsumer {

    private final ReviewProcessingService reviewProcessingService;

    public ReviewRequestConsumer(ReviewProcessingService reviewProcessingService) {
        this.reviewProcessingService = reviewProcessingService;
    }

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
            topics = "${app.kafka.topics.review-requests}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ReviewRequestedEvent event) {
        reviewProcessingService.processReview(event.reviewId());
    }

    @DltHandler
    public void handleDeadLetter(
            ReviewRequestedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        reviewProcessingService.markFailed(
                UUID.fromString(event.reviewId().toString()),
                "Review moved to DLT after repeated failures on topic " + topic
        );
    }
}
