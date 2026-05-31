package com.queryservice.executor.repository;

import com.queryservice.executor.exception.SQLExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class QueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public QueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            throw new SQLExecutionException(
                    "Failed to execute query: " + e.getMessage()
            );
        }
    }
}