package com.aicodereview.backend.service;

import com.aicodereview.backend.model.CodeReview;
import com.aicodereview.backend.model.ReviewStatus;
import com.aicodereview.backend.repository.CodeReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewProcessingService {

    private final CodeReviewRepository codeReviewRepository;
    private final ClaudeReviewClient claudeReviewClient;

    public ReviewProcessingService(
            CodeReviewRepository codeReviewRepository,
            ClaudeReviewClient claudeReviewClient
    ) {
        this.codeReviewRepository = codeReviewRepository;
        this.claudeReviewClient = claudeReviewClient;
    }

    @Transactional
    public void processReview(UUID reviewId) {
        CodeReview codeReview = getCodeReview(reviewId);
        codeReview.setStatus(ReviewStatus.PROCESSING);
        codeReview.setErrorMessage(null);
        codeReviewRepository.save(codeReview);

        String reviewResult = claudeReviewClient.reviewCode(codeReview.getLanguage(), codeReview.getSourceCode());

        codeReview.setReviewResult(reviewResult);
        codeReview.setStatus(ReviewStatus.COMPLETED);
        codeReviewRepository.save(codeReview);
    }

    @Transactional
    public void markFailed(UUID reviewId, String errorMessage) {
        CodeReview codeReview = getCodeReview(reviewId);
        codeReview.setStatus(ReviewStatus.FAILED);
        codeReview.setErrorMessage(errorMessage);
        codeReviewRepository.save(codeReview);
    }

    private CodeReview getCodeReview(UUID reviewId) {
        return codeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found for id " + reviewId));
    }
}
