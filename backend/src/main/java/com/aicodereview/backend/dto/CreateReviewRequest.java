package com.aicodereview.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @NotBlank(message = "Language is required")
        @Size(max = 50, message = "Language must be 50 characters or fewer")
        String language,
        @NotBlank(message = "Source code is required")
        @Size(max = 100_000, message = "Source code is too large for a single review")
        String sourceCode
) {
}
