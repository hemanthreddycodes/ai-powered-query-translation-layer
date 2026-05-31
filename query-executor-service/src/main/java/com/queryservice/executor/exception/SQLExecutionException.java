package com.queryservice.executor.exception;

public class SQLExecutionException extends RuntimeException {

    public SQLExecutionException(String message) {
        super(message);
    }
}