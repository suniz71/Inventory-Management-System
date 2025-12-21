package com.example.Employee.Leave.Management.System.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException e) {
        return ResponseEntity.status(404).body("Not Found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationError(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getBindingResult().getFieldErrors()
                        .stream()
                        .map(err -> err.getField() + " : " + err.getDefaultMessage())
                        .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> otherError(Exception e) {
        return ResponseEntity.status(500).body("Something went wrong");
    }
}
