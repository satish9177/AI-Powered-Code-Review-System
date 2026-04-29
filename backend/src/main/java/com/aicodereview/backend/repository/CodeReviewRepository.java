package com.aicodereview.backend.repository;

import com.aicodereview.backend.model.CodeReview;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeReviewRepository extends JpaRepository<CodeReview, UUID> {
}
