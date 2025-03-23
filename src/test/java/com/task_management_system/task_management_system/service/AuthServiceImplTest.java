package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.exception.AuthorizationException;
import com.task_management_system.task_management_system.exception.RegistrationException;
import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.security.JwtUtils;
import com.task_management_system.task_management_system.security.model.JwtResponse;
import com.task_management_system.task_management_system.security.model.LoginRequest;
import com.task_management_system.task_management_system.security.model.MessageResponse;
import com.task_management_system.task_management_system.security.model.SignupRequest;
import com.task_management_system.task_management_system.security.service.UserDetailsImpl;
import com.task_management_system.task_management_system.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authUser_ShouldAuthenticateSuccessfully() {
        // Подготовка тестовых данных
        String email = "user@example.com";
        String password = "password123";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Создаем объект пользователя
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        // Зашифрованный пароль
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        // Создаем объект роли
        Role role = new Role(ERole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Создание объекта UserDetailsImpl на основе пользователя
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // Подготовка мока для аутентификации
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Мокаем поведение репозиториев и менеджера аутентификации
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        //when(encoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtils.generateJwtToken(any())).thenReturn("dummy-jwt-token");

        // Мокаем поведение roleRepository
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));

        // Выполнение тестируемого метода
        JwtResponse response = authService.authUser(loginRequest);

        // Проверка результата
        assertNotNull(response.getToken());
        assertEquals("dummy-jwt-token", response.getToken());
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getEmail(), response.getEmail());
        assertTrue(response.getRoles().contains(role));
    }




    @Test
    void authUser_ShouldThrowException_WhenEmailNotFound() {
        // Prepare test data
        String email = "user@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Mock the behavior of dependencies
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Call the method and verify the exception
        assertThrows(AuthorizationException.class, () -> authService.authUser(loginRequest));
    }

    @Test
    void authUser_ShouldThrowException_WhenPasswordIsIncorrect() {
        // Prepare test data
        String email = "user@example.com";
        String password = "incorrectPassword";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Mock the behavior of dependencies
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Password is incorrect"));

        // Call the method and verify the exception
        assertThrows(AuthorizationException.class, () -> authService.authUser(loginRequest));
    }

    @Test
    void registerUser_ShouldRegisterUserSuccessfully() {
        // Prepare test data
        String email = "newuser@example.com";
        String password = "password123";
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);

        // Mock the behavior of dependencies
        Role defaultRole = new Role(ERole.ROLE_USER);  // Create role with ERole enum
        User user = new User(email, encoder.encode(password));

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));

        // Call the method
        MessageResponse response = authService.registerUser(signUpRequest);

        // Verify the results
        assertNotNull(response);
        assertEquals("User: newuser@example.com - registered successfully!", response.getMessage());
        verify(userRepository).save(any(User.class));  // Ensure that user is saved
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Prepare test data
        String email = "existinguser@example.com";
        String password = "password123";
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);

        // Mock the behavior of dependencies
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Call the method and verify the exception
        assertThrows(RegistrationException.class, () -> authService.registerUser(signUpRequest));
    }
}
