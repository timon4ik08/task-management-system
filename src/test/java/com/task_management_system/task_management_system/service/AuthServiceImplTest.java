package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.exception.AuthorizationException;
import com.task_management_system.task_management_system.exception.RegistrationException;
import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.security.model.JwtResponse;
import com.task_management_system.task_management_system.security.model.LoginRequest;
import com.task_management_system.task_management_system.security.model.MessageResponse;
import com.task_management_system.task_management_system.security.model.SignupRequest;
import com.task_management_system.task_management_system.security.service.UserDetailsImpl;
import com.task_management_system.task_management_system.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authUser_ShouldAuthenticateSuccessfully() {
        String email = "user@example.com";
        String password = "password123";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        Role role = new Role(ERole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any())).thenReturn("dummy-jwt-token");

        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));

        JwtResponse response = authService.authUser(loginRequest);

        assertNotNull(response.getToken());
        assertEquals("dummy-jwt-token", response.getToken());
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getEmail(), response.getEmail());
        assertTrue(response.getRoles().contains(role));
    }


    @Test
    void authUser_ShouldThrowException_WhenEmailNotFound() {
        String email = "user@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThrows(AuthorizationException.class, () -> authService.authUser(loginRequest));
    }

    @Test
    void authUser_ShouldThrowException_WhenPasswordIsIncorrect() {
        String email = "user@example.com";
        String password = "incorrectPassword";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Password is incorrect"));

        assertThrows(AuthorizationException.class, () -> authService.authUser(loginRequest));
    }

    @Test
    void registerUser_ShouldRegisterUserSuccessfully() {
        String email = "newuser@example.com";
        String password = "password123";
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);

        Role defaultRole = new Role(ERole.ROLE_USER);
        User user = new User(email, encoder.encode(password));

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));

        MessageResponse response = authService.registerUser(signUpRequest);

        assertNotNull(response);
        assertEquals("User: newuser@example.com - registered successfully!", response.getMessage());
        verify(userRepository).save(any(User.class));  // Ensure that user is saved
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        String email = "existinguser@example.com";
        String password = "password123";
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(RegistrationException.class, () -> authService.registerUser(signUpRequest));
    }
}
