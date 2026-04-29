package com.aicodereview.backend.service;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildReviewPrompt(String language, String sourceCode) {
        return """
                You are a senior software engineer performing a code review.

                Review the submitted code and provide:
                1. A short overall summary
                2. Potential bugs
                3. Code smells / maintainability concerns
                4. Performance issues
                5. Security concerns
                6. Improvement suggestions

                Rules:
                - Be precise and actionable.
                - If something looks fine, say so briefly.
                - Do not invent framework details that are not visible in the code.
                - Keep the output readable in markdown.

                Language:
                %s

                Code:
                ```%s
                %s
                ```
                """.formatted(language, language.toLowerCase(), sourceCode);
    }
}
