package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDTO getCurrentUserId(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);

    UserDTO updateUserRoles(Long userId, UpdateRolesRequest request);

    Page<UserDTO> getAllUsers(Pageable pageable);
}
