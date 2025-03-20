package com.task_management_system.task_management_system.security.exception.handler;

import com.task_management_system.task_management_system.exception.ResponseException;
import com.task_management_system.task_management_system.security.exception.AuthorizationException;
import com.task_management_system.task_management_system.security.exception.RegistrationException;
import com.task_management_system.task_management_system.exception.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SecurityExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<?> handlerAuthorizationException(AuthorizationException ex) {
        ResponseException responseException = new ResponseException.Builder()
                .setMessage(ex.getMessage())
                .setTitle("Authorization Error: ")
                .setStackTrace(ex.getStackTrace())
                .build();
        return new ResponseEntity<>(responseException.toString(), ResponseHeader.headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<?> handlerRegistrationException(RegistrationException ex) {
        ResponseException responseException = new ResponseException.Builder()
                .setMessage(ex.getMessage())
                .setTitle("Registration Error: ")
                .setStackTrace(ex.getStackTrace())
                .build();
        return new ResponseEntity<>(responseException.toString(), ResponseHeader.headers, HttpStatus.BAD_REQUEST);
    }
}
