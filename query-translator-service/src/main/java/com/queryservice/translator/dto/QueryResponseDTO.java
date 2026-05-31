package com.queryservice.translator.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class QueryResponseDTO {

    private String originalQuery;
    private String generatedSql;
    private List<Map<String, Object>> results;
    private int rowCount;
    private LocalDateTime timestamp;

    // No args constructor
    public QueryResponseDTO() {}

    // All args constructor
    public QueryResponseDTO(String originalQuery,
                            String generatedSql,
                            List<Map<String, Object>> results,
                            int rowCount,
                            LocalDateTime timestamp) {
        this.originalQuery = originalQuery;
        this.generatedSql = generatedSql;
        this.results = results;
        this.rowCount = rowCount;
        this.timestamp = timestamp;
    }

    // Getters
    public String getOriginalQuery() { return originalQuery; }
    public String getGeneratedSql() { return generatedSql; }
    public List<Map<String, Object>> getResults() { return results; }
    public int getRowCount() { return rowCount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }
    public void setGeneratedSql(String generatedSql) {
        this.generatedSql = generatedSql;
    }
    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }
    public void setRowCount(int rowCount) { this.rowCount = rowCount; }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}