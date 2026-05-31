package com.queryservice.executor.exception;

import com.queryservice.executor.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSQLException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidSQL(
            InvalidSQLException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Invalid SQL",
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SQLExecutionException.class)
    public ResponseEntity<ErrorResponseDTO> handleSQLExecution(
            SQLExecutionException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "SQL Execution Failed",
                ex.getMessage(),
                500,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneral(
            Exception ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Internal Server Error",
                ex.getMessage(),
                500,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Validation Failed",
                message,
                400,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}