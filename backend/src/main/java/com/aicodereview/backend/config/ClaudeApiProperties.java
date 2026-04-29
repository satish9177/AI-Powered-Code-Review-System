package com.aicodereview.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "claude.api")
public record ClaudeApiProperties(
        String baseUrl,
        String key,
        String model,
        String anthropicVersion,
        Integer maxTokens
) {
}
