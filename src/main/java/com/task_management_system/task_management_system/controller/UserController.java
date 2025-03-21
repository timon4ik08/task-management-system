package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.AdminOnly;
import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import com.task_management_system.task_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @AdminOnly
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO userDTO = userService.getCurrentUserId(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@CurrentUser UserDTO userDTO,
                                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userDTO.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/roles")
    @AdminOnly
    public ResponseEntity<UserDTO> updateRoles(@PathVariable Long userId,
                                            @RequestBody UpdateRolesRequest request) {
        UserDTO userDTO = userService.updateUserRoles(userId, request);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    @AdminOnly
    public ResponseEntity<Page<UserDTO>> getAllUsers(@PageableDefault(size = 5,
            sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
}