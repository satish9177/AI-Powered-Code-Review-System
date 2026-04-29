package com.aicodereview.backend.service;

import com.aicodereview.backend.dto.CreateReviewRequest;
import com.aicodereview.backend.dto.CreateReviewResponse;
import com.aicodereview.backend.dto.ReviewResponse;
import com.aicodereview.backend.messaging.ReviewRequestProducer;
import com.aicodereview.backend.messaging.ReviewRequestedEvent;
import com.aicodereview.backend.model.CodeReview;
import com.aicodereview.backend.model.ReviewStatus;
import com.aicodereview.backend.repository.CodeReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CodeReviewService {

    private final CodeReviewRepository codeReviewRepository;
    private final ReviewRequestProducer reviewRequestProducer;

    public CodeReviewService(
            CodeReviewRepository codeReviewRepository,
            ReviewRequestProducer reviewRequestProducer
    ) {
        this.codeReviewRepository = codeReviewRepository;
        this.reviewRequestProducer = reviewRequestProducer;
    }

    @Transactional
    public CreateReviewResponse submitReview(CreateReviewRequest request) {
        CodeReview codeReview = new CodeReview();
        codeReview.setId(UUID.randomUUID());
        codeReview.setLanguage(request.language().trim());
        codeReview.setSourceCode(request.sourceCode());
        codeReview.setStatus(ReviewStatus.PENDING);

        codeReviewRepository.save(codeReview);

        reviewRequestProducer.publish(new ReviewRequestedEvent(
                codeReview.getId(),
                codeReview.getLanguage(),
                Instant.now()
        ));

        return new CreateReviewResponse(codeReview.getId(), codeReview.getStatus());
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(UUID reviewId) {
        CodeReview codeReview = codeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found for id " + reviewId));

        return new ReviewResponse(
                codeReview.getId(),
                codeReview.getLanguage(),
                codeReview.getStatus(),
                codeReview.getReviewResult(),
                codeReview.getErrorMessage(),
                codeReview.getCreatedAt(),
                codeReview.getUpdatedAt()
        );
    }
}
