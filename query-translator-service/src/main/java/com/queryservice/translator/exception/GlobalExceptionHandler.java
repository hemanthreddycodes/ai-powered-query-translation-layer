package com.queryservice.translator.exception;

import com.queryservice.translator.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidQueryException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidQuery(
            InvalidQueryException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Invalid Query",
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TranslationFailedException.class)
    public ResponseEntity<ErrorResponseDTO> handleTranslationFailed(
            TranslationFailedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Translation Failed",
                ex.getMessage(),
                422,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
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
}