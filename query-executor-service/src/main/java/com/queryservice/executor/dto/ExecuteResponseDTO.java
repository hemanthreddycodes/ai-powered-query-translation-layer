package com.queryservice.executor.dto;

import java.util.List;
import java.util.Map;

public class ExecuteResponseDTO {

    private List<Map<String, Object>> results;
    private int rowCount;

    // No args constructor
    public ExecuteResponseDTO() {}

    // All args constructor
    public ExecuteResponseDTO(List<Map<String, Object>> results,
                              int rowCount) {
        this.results = results;
        this.rowCount = rowCount;
    }

    // Getters
    public List<Map<String, Object>> getResults() { return results; }
    public int getRowCount() { return rowCount; }

    // Setters
    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }
    public void setRowCount(int rowCount) { this.rowCount = rowCount; }
}