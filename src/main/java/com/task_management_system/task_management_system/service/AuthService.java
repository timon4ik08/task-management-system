package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.security.model.JwtResponse;
import com.task_management_system.task_management_system.security.model.LoginRequest;
import com.task_management_system.task_management_system.security.model.MessageResponse;
import com.task_management_system.task_management_system.security.model.SignupRequest;

public interface AuthService {
    JwtResponse authUser(LoginRequest loginRequest);

    MessageResponse registerUser(SignupRequest signUpRequest);
}
