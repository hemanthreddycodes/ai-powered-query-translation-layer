package com.queryservice.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class QueryRequestDTO {

    @NotBlank(message = "Query cannot be empty")
    //@Size(min = 3, max = 200,
           // message = "Query must be between 3 and 200 characters")
    private String query;

    // No args constructor
    public QueryRequestDTO() {}

    // All args constructor
    public QueryRequestDTO(String query) {
        this.query = query;
    }

    // Getter
    public String getQuery() { return query; }

    // Setter
    public void setQuery(String query) { this.query = query; }
}