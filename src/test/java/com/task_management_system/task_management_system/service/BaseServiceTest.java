package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.repository.CommentRepository;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.TaskRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.security.JwtUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class BaseServiceTest {
    @Mock
    protected AuthenticationManager authenticationManager;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected RoleRepository roleRepository;

    @Mock
    protected PasswordEncoder encoder;

    @Mock
    protected JwtUtils jwtUtils;

    @Mock
    protected CommentRepository commentRepository;

    @Mock
    protected TaskRepository taskRepository;
}
