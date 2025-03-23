package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.security.model.LoginRequest;
import com.task_management_system.task_management_system.security.model.SignupRequest;
import com.task_management_system.task_management_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "API for user authentication and registration")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    @Operation(summary = "Authenticate user", description = "Allows a user to log in to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication"),
            @ApiResponse(responseCode = "400", description = "Invalid login data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Authenticating user. Login request: {}", loginRequest.getEmail());
        ResponseEntity<?> response = ResponseEntity.ok(authService.authUser(loginRequest));
        log.debug("User authenticated successfully: {}", loginRequest.toString());
        return response;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user", description = "Allows a new user to register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info("Registering new user. Signup request: {}", signUpRequest.getEmail());
        ResponseEntity<?> response = ResponseEntity.ok(authService.registerUser(signUpRequest));
        log.debug("User registered successfully: {}", signUpRequest);
        return response;
    }
}