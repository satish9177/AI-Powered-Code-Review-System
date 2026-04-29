package com.aicodereview.backend.service.impl;

import com.aicodereview.backend.config.ClaudeApiProperties;
import com.aicodereview.backend.service.ClaudeReviewClient;
import com.aicodereview.backend.service.PromptBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Component
public class AnthropicClaudeReviewClient implements ClaudeReviewClient {

    private final RestClient restClient;
    private final ClaudeApiProperties claudeApiProperties;
    private final PromptBuilder promptBuilder;

    public AnthropicClaudeReviewClient(
            RestClient restClient,
            ClaudeApiProperties claudeApiProperties,
            PromptBuilder promptBuilder
    ) {
        this.restClient = restClient;
        this.claudeApiProperties = claudeApiProperties;
        this.promptBuilder = promptBuilder;
    }

    @Override
    public String reviewCode(String language, String sourceCode) {
        if (!StringUtils.hasText(claudeApiProperties.key())) {
            throw new IllegalStateException("Claude API key is not configured");
        }

        String prompt = promptBuilder.buildReviewPrompt(language, sourceCode);

        JsonNode response = restClient.post()
                .uri(claudeApiProperties.baseUrl() + "/v1/messages")
                .header("x-api-key", claudeApiProperties.key())
                .header("anthropic-version", claudeApiProperties.anthropicVersion())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ClaudeRequest(
                        claudeApiProperties.model(),
                        claudeApiProperties.maxTokens(),
                        new ClaudeMessage[] {
                                new ClaudeMessage("user", prompt)
                        }
                ))
                .retrieve()
                .body(JsonNode.class);

        JsonNode content = response.path("content");
        if (!content.isArray() || content.isEmpty()) {
            throw new IllegalStateException("Claude response content was empty");
        }

        return content.get(0).path("text").asText("No review content returned.");
    }

    private record ClaudeRequest(String model, int max_tokens, ClaudeMessage[] messages) {
    }

    private record ClaudeMessage(String role, String content) {
    }
}
