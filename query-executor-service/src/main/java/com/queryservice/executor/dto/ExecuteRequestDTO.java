package com.queryservice.executor.dto;

import jakarta.validation.constraints.NotBlank;

public class ExecuteRequestDTO {

    @NotBlank(message = "SQL query cannot be empty")
    private String sql;

    // No args constructor
    public ExecuteRequestDTO() {}

    // All args constructor
    public ExecuteRequestDTO(String sql) {
        this.sql = sql;
    }

    // Getter
    public String getSql() { return sql; }

    // Setter
    public void setSql(String sql) { this.sql = sql; }
}