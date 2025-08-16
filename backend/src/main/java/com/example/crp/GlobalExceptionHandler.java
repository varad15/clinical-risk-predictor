package com.example.crp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> details = new HashMap<>();
        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            details.put(f.getField(), f.getDefaultMessage());
        }
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", details.toString());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getMessage().contains("Training in progress")) {
            status = HttpStatus.CONFLICT;
        } else if (ex.getMessage().contains("Model not loaded")) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }
        return new ResponseEntity<>(new ErrorResponse(status.value(), ex.getMessage(), null), status);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
    }

    public record ErrorResponse(int code, String message, String details) {}
}
