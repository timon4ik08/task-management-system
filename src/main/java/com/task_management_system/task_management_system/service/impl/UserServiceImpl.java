package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.exception.InvalidPasswordException;
import com.task_management_system.task_management_system.exception.RoleException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import com.task_management_system.task_management_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper = new ModelMapper();

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        log.info("UserServiceImpl initialized with dependencies");
    }

    @Override
    public UserDTO getCurrentUserId(Long userId) {
        log.info("Attempting to fetch user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserException("Not found user with id = " + userId);
                });
        log.info("Successfully fetched user with ID: {}", userId);
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        log.info("Attempting to change password for user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserException("Not found user with id = " + userId);
                });

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.warn("Incorrect old password provided for user with ID: {}", userId);
            throw new InvalidPasswordException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password successfully changed for user with ID: {}", userId);
    }

    @Override
    public UserDTO updateUserRoles(Long userId, UpdateRolesRequest request) {
        log.info("Attempting to update roles for user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new UserException("Not found user with id = " + userId);
                });

        Set<Role> newRoles = request.getRoles().stream()
                .map(roleName -> {
                    log.debug("Looking up role: {}", roleName);
                    return roleRepository.findByName(ERole.valueOf(roleName))
                            .orElseThrow(() -> {
                                log.error("Role not found: {}", roleName);
                                return new RoleException("Role not found: " + roleName);
                            });
                })
                .collect(Collectors.toSet());

        user.setRoles(newRoles);
        User userSave = userRepository.save(user);
        log.info("Roles successfully updated for user with ID: {}", userId);
        return mapper.map(userSave, UserDTO.class);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        log.info("Fetching all users with pagination: {}", pageable);
        Page<User> userPages = userRepository.findAll(pageable);
        log.info("Successfully fetched {} users", userPages.getTotalElements());
        return userPages.map(p -> mapper.map(p, UserDTO.class));
    }
}