package com.task_management_system.task_management_system.exception.handler;


import com.task_management_system.task_management_system.exception.ResponseException;
import com.task_management_system.task_management_system.exception.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            joiner.add("field - '" + fieldName + "' " + errorMessage);
        });

        String message = joiner.toString();
        return new ResponseEntity<>(new ResponseException("Input data error:", message).toString(),
                ResponseHeader.headers,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {

        return new ResponseEntity<>(
                new ResponseException("Resource not found:", "The requested resource " + ex.getRequestURL()
                        + " was not found").toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }
}
