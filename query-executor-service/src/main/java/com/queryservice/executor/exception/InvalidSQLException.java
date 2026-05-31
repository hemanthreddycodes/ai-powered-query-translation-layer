package com.queryservice.executor.exception;

public class InvalidSQLException extends RuntimeException {

    public InvalidSQLException(String message) {
        super(message);
    }
}