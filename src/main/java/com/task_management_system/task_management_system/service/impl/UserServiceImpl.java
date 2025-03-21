package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import com.task_management_system.task_management_system.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO getCurrentUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found user"));
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDTO updateUserRoles(Long userId, UpdateRolesRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> newRoles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);
        User userSave = userRepository.save(user);
        return mapper.map(userSave, UserDTO.class);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> userPages = userRepository.findAll(pageable);
        return userPages.map(p -> mapper.map(p, UserDTO.class));
    }
}
