package com.queryservice.translator.service;

import com.queryservice.translator.config.ClaudeApiConfig;
import com.queryservice.translator.dto.ExecuteResponseDTO;
import com.queryservice.translator.dto.QueryRequestDTO;
import com.queryservice.translator.dto.QueryResponseDTO;
import com.queryservice.translator.exception.TranslationFailedException;
import com.queryservice.translator.model.QueryLog;
import com.queryservice.translator.repository.QueryLogRepository;
import com.queryservice.translator.util.ClaudeResponseParser;
import com.queryservice.translator.validator.QueryValidator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
public class TranslatorService {

    private final QueryValidator queryValidator;
    private final ClaudeResponseParser claudeResponseParser;
    private final QueryLogRepository queryLogRepository;
    private final RestTemplate restTemplate;
    private final ClaudeApiConfig claudeApiConfig;

    public TranslatorService(
            QueryValidator queryValidator,
            ClaudeResponseParser claudeResponseParser,
            QueryLogRepository queryLogRepository,
            RestTemplate restTemplate,
            ClaudeApiConfig claudeApiConfig) {
        this.queryValidator = queryValidator;
        this.claudeResponseParser = claudeResponseParser;
        this.queryLogRepository = queryLogRepository;
        this.restTemplate = restTemplate;
        this.claudeApiConfig = claudeApiConfig;
    }

    public QueryResponseDTO translate(
            QueryRequestDTO request, String username) {

        String query = request.getQuery();
        String generatedSql = null;
        String status = "FAILED";

        try {
            // Step 1 — Validate English query
            queryValidator.validate(query);

            // Step 2 — Call Claude API
            generatedSql = callClaudeAPI(query);

            // Step 3 — Call Executor Service
            ExecuteResponseDTO executeResponse =
                    callExecutorService(generatedSql);

            status = "SUCCESS";

            // Step 4 — Return response
            return new QueryResponseDTO(
                    query,
                    generatedSql,
                    executeResponse.getResults(),
                    executeResponse.getRowCount(),
                    LocalDateTime.now()
            );

        } finally {
            // Step 5 — Always save query log
            saveQueryLog(query, generatedSql,
                    status, username);
        }
    }

    private String callClaudeAPI(String query) {
        try {
            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key",
                    claudeApiConfig.getApiKey());
            headers.set("anthropic-version", "2023-06-01");

            // Build request body
            String requestBody = """
                {
                    "model": "%s",
                    "max_tokens": 1000,
                    "messages": [
                        {
                            "role": "user",
                            "content": "Convert this to SQL for a products table with columns (id, name, price, category). Return ONLY the SQL query, nothing else: %s"
                        }
                    ]
                }
                """.formatted(
                    claudeApiConfig.getModel(), query);

            // Make HTTP call to Claude
            HttpEntity<String> entity =
                    new HttpEntity<>(requestBody, headers);

            String response = restTemplate.postForObject(
                    claudeApiConfig.getApiUrl(),
                    entity,
                    String.class
            );

            // Extract SQL from response
            return claudeResponseParser.extractSQL(response);

        } catch (TranslationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new TranslationFailedException(
                    "Claude API call failed: " + e.getMessage()
            );
        }
    }

    private ExecuteResponseDTO callExecutorService(
            String sql) {
        try {
            String executorUrl =
                    "http://localhost:8082/api/executor/execute";

            String requestBody =
                    "{\"sql\": \"" + sql + "\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity =
                    new HttpEntity<>(requestBody, headers);

            return restTemplate.postForObject(
                    executorUrl,
                    entity,
                    ExecuteResponseDTO.class
            );

        } catch (Exception e) {
            throw new TranslationFailedException(
                    "Executor service call failed: "
                            + e.getMessage()
            );
        }
    }

    private void saveQueryLog(String query,
                              String sql,
                              String status,
                              String username) {
        try {
            QueryLog log = new QueryLog();
            log.setOriginalQuery(query);
            log.setGeneratedSql(
                    sql != null ? sql : "SQL generation failed"
            );
            log.setStatus(status);
            log.setUsername(username);
            log.setCreatedAt(LocalDateTime.now());
            queryLogRepository.save(log);
        } catch (Exception e) {
            // Log saving should never crash the app
        }
    }
}