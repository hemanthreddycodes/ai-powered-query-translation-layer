package com.queryservice.translator.dto;

public class LoginResponseDTO {

    private String token;
    private String message;

    // No args constructor
    public LoginResponseDTO() {}

    // All args constructor
    public LoginResponseDTO(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters
    public String getToken() { return token; }
    public String getMessage() { return message; }

    // Setters
    public void setToken(String token) { this.token = token; }
    public void setMessage(String message) {
        this.message = message;
    }
}