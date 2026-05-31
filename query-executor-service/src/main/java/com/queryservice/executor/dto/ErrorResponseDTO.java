package com.queryservice.executor.dto;

import java.time.LocalDateTime;

public class ErrorResponseDTO {

    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;

    // No args constructor
    public ErrorResponseDTO() {}

    // All args constructor
    public ErrorResponseDTO(String error, String message,
                            int status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getError() { return error; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setError(String error) { this.error = error; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(int status) { this.status = status; }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}