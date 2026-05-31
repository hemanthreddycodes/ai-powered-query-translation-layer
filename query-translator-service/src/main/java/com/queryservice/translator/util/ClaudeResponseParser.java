package com.queryservice.translator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.queryservice.translator.exception.TranslationFailedException;
import org.springframework.stereotype.Component;

@Component
public class ClaudeResponseParser {

    private final ObjectMapper objectMapper;

    public ClaudeResponseParser() {
        this.objectMapper = new ObjectMapper();
    }

    public String extractSQL(String response) {
        try {
            //Parse Claude's JSON response
            JsonNode root = objectMapper.readTree(response);

            //Extract text from response
            String sql = root
                    .path("content")
                    .get(0)
                    .path("text")
                    .asText();

            //Check if SQL is empty
            if (sql == null || sql.trim().isEmpty()) {
                throw new TranslationFailedException(
                        "Claude returned empty response"
                );
            }

            //Clean the SQL
            sql = sql.trim();

            //Remove markdown code blocks if Claude adds them
            if (sql.startsWith("```sql")) {
                sql = sql.substring(6);
            }
            if (sql.startsWith("```")) {
                sql = sql.substring(3);
            }
            if (sql.endsWith("```")) {
                sql = sql.substring(0, sql.length() - 3);
            }

            return sql.trim();

        } catch (TranslationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new TranslationFailedException(
                    "Failed to parse Claude response: "
                            + e.getMessage()
            );
        }
    }
}