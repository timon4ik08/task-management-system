package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.exception.InvalidPasswordException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import com.task_management_system.task_management_system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
    }

    @Test
    void getCurrentUserId_ShouldReturnUserDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getCurrentUserId(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getCurrentUserId_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.getCurrentUserId(1L));
    }

    @Test
    void changePassword_ShouldChangePasswordSuccessfully() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("password");
        request.setNewPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encoder.matches("password", user.getPassword())).thenReturn(true);
        when(encoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.changePassword(1L, request);

        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void changePassword_ShouldThrowException_WhenOldPasswordIsIncorrect() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("incorrectPassword");
        request.setNewPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encoder.matches("incorrectPassword", user.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(1L, request));
    }

    @Test
    void updateUserRoles_ShouldUpdateRolesSuccessfully() {
        UpdateRolesRequest request = new UpdateRolesRequest();
        request.setRoles(Set.of(ERole.ROLE_USER.name()));

        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUserRoles(1L, request);

        assertNotNull(result);
        assertTrue(result.getRoles().stream().anyMatch(r->r.contains("ROLE_USER")));
    }

    @Test
    void updateUserRoles_ShouldThrowException_WhenRoleNotFound() {
        UpdateRolesRequest request = new UpdateRolesRequest();
        request.setRoles(Set.of("NON_EXISTING_ROLE"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.updateUserRoles(1L, request));
    }

    @Test
    void getAllUsers_ShouldReturnPagedUsers() {
        Page<User> users = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(users);

        Page<UserDTO> result = userService.getAllUsers(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}

