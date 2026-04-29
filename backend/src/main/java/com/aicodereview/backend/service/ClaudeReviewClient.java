package com.aicodereview.backend.service;

public interface ClaudeReviewClient {

    String reviewCode(String language, String sourceCode);
}
