package com.queryservice.executor.validator;

import com.queryservice.executor.exception.InvalidSQLException;
import org.springframework.stereotype.Component;

@Component
public class SQLValidator {

    public void validate(String sql) {

        // Check SQL is not null or empty
        if (sql == null || sql.trim().isEmpty()) {
            throw new InvalidSQLException(
                    "SQL query cannot be empty"
            );
        }

        // Only SELECT allowed
        if (!sql.trim().toUpperCase().startsWith("SELECT")) {
            throw new InvalidSQLException(
                    "Only SELECT queries are allowed"
            );
        }

        // Only products table allowed
        if (!sql.toUpperCase().contains("FROM PRODUCTS")) {
            throw new InvalidSQLException(
                    "Only products table can be queried"
            );
        }

        // No multiple statements allowed
        if (sql.contains(";") &&
                sql.indexOf(";") < sql.length() - 1) {
            throw new InvalidSQLException(
                    "Multiple SQL statements are not allowed"
            );
        }

        // No SQL injection patterns
        String upperSql = sql.toUpperCase();
        if (upperSql.contains("DROP") ||
                upperSql.contains("DELETE") ||
                upperSql.contains("UPDATE") ||
                upperSql.contains("INSERT") ||
                upperSql.contains("TRUNCATE") ||
                upperSql.contains("ALTER") ||
                upperSql.contains("CREATE")) {
            throw new InvalidSQLException(
                    "Dangerous SQL keywords detected"
            );
        }
    }
}