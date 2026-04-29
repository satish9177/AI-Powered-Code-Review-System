package com.aicodereview.backend.messaging;

import java.time.Instant;
import java.util.UUID;

public record ReviewRequestedEvent(
        UUID reviewId,
        String language,
        Instant submittedAt
) {
}
