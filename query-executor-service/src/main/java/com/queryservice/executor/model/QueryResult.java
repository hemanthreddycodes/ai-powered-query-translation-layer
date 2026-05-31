package com.queryservice.executor.model;

import java.util.List;
import java.util.Map;

public class QueryResult {

    private String sql;
    private List<Map<String, Object>> results;
    private int rowCount;

    // No args constructor
    public QueryResult() {}

    // All args constructor
    public QueryResult(String sql,
                       List<Map<String, Object>> results,
                       int rowCount) {
        this.sql = sql;
        this.results = results;
        this.rowCount = rowCount;
    }

    // Getters
    public String getSql() { return sql; }
    public List<Map<String, Object>> getResults() { return results; }
    public int getRowCount() { return rowCount; }

    // Setters
    public void setSql(String sql) { this.sql = sql; }
    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }
    public void setRowCount(int rowCount) { this.rowCount = rowCount; }
}