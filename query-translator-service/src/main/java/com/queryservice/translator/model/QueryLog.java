package com.queryservice.translator.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "query_logs")
public class QueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_query", nullable = false)
    private String originalQuery;

    @Column(name = "generated_sql", nullable = false)
    private String generatedSql;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String username;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // No args constructor
    public QueryLog() {}

    // All args constructor
    public QueryLog(Long id, String originalQuery,
                    String generatedSql, String status,
                    String username, LocalDateTime createdAt) {
        this.id = id;
        this.originalQuery = originalQuery;
        this.generatedSql = generatedSql;
        this.status = status;
        this.username = username;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getOriginalQuery() { return originalQuery; }
    public String getGeneratedSql() { return generatedSql; }
    public String getStatus() { return status; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }
    public void setGeneratedSql(String generatedSql) {
        this.generatedSql = generatedSql;
    }
    public void setStatus(String status) { this.status = status; }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}