package com.task_management_system.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerControllerTest extends BaseControllerTest {

    @Test
    void getUser_Success() throws Exception {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setEmail("test@example.com");
        userDTO.setRoles(Set.of("ROLE_USER"));

        when(userService.getCurrentUserId(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/{userId}", userId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUser_NotFound() throws Exception {
        Long userId = 1L;
        when(userService.getCurrentUserId(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/users/{userId}", userId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRoles_Success() throws Exception {
        Long userId = 1L;
        UpdateRolesRequest request = new UpdateRolesRequest();
        request.setRoles(Set.of("ROLE_ADMIN"));
        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(userId);
        updatedUser.setRoles(request.getRoles());

        when(userService.updateUserRoles(eq(userId), any(UpdateRolesRequest.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_Success() throws Exception {

        Page<UserDTO> page = new PageImpl<>(List.of(new UserDTO(1L, "admin@example.com", Collections.EMPTY_SET, null)));

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].email").value("admin@example.com"));
    }
}