package com.junyounggoat.dreamstore.user.api.controller;

import com.junyounggoat.dreamstore.user.api.dto.BadRequestDTO;
import com.junyounggoat.dreamstore.user.api.validation.NotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        List<String> notFieldErrors = new LinkedList<>();

        exception.getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                fieldErrors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                notFieldErrors.add(error.getDefaultMessage());
            }
        });

        return ResponseEntity.badRequest().body(BadRequestDTO.builder().fieldErrors(fieldErrors).notFieldErrors(notFieldErrors).build());
    }

    @ExceptionHandler(NotValidException.class)
    public ResponseEntity<BadRequestDTO> handleMethodArgumentNotValidException(NotValidException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        List<String> notFieldErrors = new LinkedList<>();

        exception.getErrors().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                fieldErrors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                notFieldErrors.add(error.getDefaultMessage());
            }
        });

        return ResponseEntity.badRequest().body(BadRequestDTO.builder().fieldErrors(fieldErrors).notFieldErrors(notFieldErrors).build());
    }
}
