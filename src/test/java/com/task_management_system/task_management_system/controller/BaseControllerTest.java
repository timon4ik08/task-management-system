package com.task_management_system.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.service.UserDetailsImpl;
import com.task_management_system.task_management_system.service.AuthService;
import com.task_management_system.task_management_system.service.CommentService;
import com.task_management_system.task_management_system.service.TaskService;
import com.task_management_system.task_management_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {
    @MockBean
    protected CommentService commentService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected TaskService taskService;

    @MockBean
    protected UserService userService;

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        auth();
    }

    protected void auth(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");
        userDTO.setRoles(Set.of("ROLE_USER"));

        UserDetailsImpl userDetails = new UserDetailsImpl(
                userDTO.getId(),
                userDTO.getEmail(),
                "password",
                Collections.EMPTY_SET
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
    }
}
