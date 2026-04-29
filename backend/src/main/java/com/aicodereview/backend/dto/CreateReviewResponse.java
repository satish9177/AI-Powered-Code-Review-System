package com.aicodereview.backend.dto;

import com.aicodereview.backend.model.ReviewStatus;
import java.util.UUID;

public record CreateReviewResponse(UUID reviewId, ReviewStatus status) {
}
