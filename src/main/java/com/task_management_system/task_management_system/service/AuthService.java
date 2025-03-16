package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.model.dto.JwtResponse;
import com.task_management_system.task_management_system.model.dto.LoginRequest;
import com.task_management_system.task_management_system.model.dto.MessageResponse;
import com.task_management_system.task_management_system.model.dto.SignupRequest;

public interface AuthService {
    JwtResponse authUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
}
