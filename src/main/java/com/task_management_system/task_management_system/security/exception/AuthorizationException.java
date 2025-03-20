package com.task_management_system.task_management_system.security.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message){
        super(message);
    }
}
