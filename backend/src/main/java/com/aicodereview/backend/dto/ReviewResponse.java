package com.aicodereview.backend.dto;

import com.aicodereview.backend.model.ReviewStatus;
import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        String language,
        ReviewStatus status,
        String reviewResult,
        String errorMessage,
        Instant createdAt,
        Instant updatedAt
) {
}
