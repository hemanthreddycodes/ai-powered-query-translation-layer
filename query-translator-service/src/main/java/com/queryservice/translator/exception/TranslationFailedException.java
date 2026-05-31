package com.queryservice.translator.exception;

public class TranslationFailedException extends RuntimeException {

    public TranslationFailedException(String message) {
        super(message);
    }
}