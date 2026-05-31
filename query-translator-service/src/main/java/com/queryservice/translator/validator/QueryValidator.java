package com.queryservice.translator.validator;


import com.queryservice.translator.exception.InvalidQueryException;
import org.springframework.stereotype.Component;

@Component
public class QueryValidator {

    public void validate(String query) {
        //check query is not null or empty
        if (query == null || query.trim().isEmpty()) {
            throw new InvalidQueryException("Query cannot be empty");
        }

        //check minimum length
        if (query.trim().length() < 3) {
            throw new InvalidQueryException("Query is too short-minimum 3 characters");
        }

        //check maximum length
        if (query.trim().length() > 200) {
            throw new InvalidQueryException("Query is too long - maximum 200 characters");
        }

        //check for suspicious patterns
        String upperQuery = query.toUpperCase();
        if (upperQuery.contains("DROP")||
            upperQuery.contains("DELETE")||
            upperQuery.contains("TRUNCATE")||
            upperQuery.contains("INSERT")||
            upperQuery.contains("UPDATE")) {
            throw new InvalidQueryException("Query contains suspicious keywords");
        }
    }
}
