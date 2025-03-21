package com.task_management_system.task_management_system.exception.handler;


import com.task_management_system.task_management_system.exception.CommentException;
import com.task_management_system.task_management_system.exception.RoleException;
import com.task_management_system.task_management_system.exception.TaskException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.exception.response.ResponseException;
import com.task_management_system.task_management_system.exception.response.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<String> handleCommentException(CommentException ex) {

        return new ResponseEntity<>(
                new ResponseException("Comment error: ", ex.getMessage()).toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<String> handleRoleException(RoleException ex) {

        return new ResponseEntity<>(
                new ResponseException("Role error: ", ex.getMessage()).toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<String> handleTaskException(TaskException ex) {

        return new ResponseEntity<>(
                new ResponseException("Task error: ", ex.getMessage()).toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {

        return new ResponseEntity<>(
                new ResponseException("User error: ", ex.getMessage()).toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {

        return new ResponseEntity<>(
                new ResponseException("AccessDenied error: ", ex.getMessage()).toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {

        return new ResponseEntity<>(
                new ResponseException("IllegalArgument error: ", ex.getMessage()).toString(),
                ResponseHeader.headers,
                HttpStatus.NOT_FOUND);
    }
}
