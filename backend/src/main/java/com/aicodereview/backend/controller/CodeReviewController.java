package com.aicodereview.backend.controller;

import com.aicodereview.backend.dto.CreateReviewRequest;
import com.aicodereview.backend.dto.CreateReviewResponse;
import com.aicodereview.backend.dto.ReviewResponse;
import com.aicodereview.backend.service.CodeReviewService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class CodeReviewController {

    private final CodeReviewService codeReviewService;

    public CodeReviewController(CodeReviewService codeReviewService) {
        this.codeReviewService = codeReviewService;
    }

    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        CreateReviewResponse response = codeReviewService.submitReview(request);
        return ResponseEntity.accepted()
                .location(URI.create("/api/reviews/" + response.reviewId()))
                .body(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID reviewId) {
        return ResponseEntity.ok(codeReviewService.getReview(reviewId));
    }
}
