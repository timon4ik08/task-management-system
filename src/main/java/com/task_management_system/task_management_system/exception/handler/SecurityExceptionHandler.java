package com.task_management_system.task_management_system.exception.handler;

import com.task_management_system.task_management_system.exception.InvalidPasswordException;
import com.task_management_system.task_management_system.exception.response.ResponseException;
import com.task_management_system.task_management_system.exception.AuthorizationException;
import com.task_management_system.task_management_system.exception.RegistrationException;
import com.task_management_system.task_management_system.exception.response.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;

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
        return new ResponseEntity<>(responseException.toString(), ResponseHeader.headers, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handlerInvalidParameterException(InvalidPasswordException ex) {
        ResponseException responseException = new ResponseException.Builder()
                .setMessage(ex.getMessage())
                .setTitle("Invalid password: ")
                .setStackTrace(ex.getStackTrace())
                .build();
        return new ResponseEntity<>(responseException.toString(), ResponseHeader.headers, HttpStatus.BAD_REQUEST);
    }
}
