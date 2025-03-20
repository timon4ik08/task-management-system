//package com.task_management_system.task_management_system.controller;
//
//import com.task_management_system.task_management_system.annotation.AdminOnly;
//import com.task_management_system.task_management_system.annotation.CurrentUser;
//import com.task_management_system.task_management_system.model.dto.UserDTO;
//import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
//import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
//import com.task_management_system.task_management_system.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("api/user")
//class UserController {
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PatchMapping("/change-password")
//    public ResponseEntity<Void> changePassword(@CurrentUser UserDTO userDTO,
//                                               @RequestBody ChangePasswordRequest request) {
//        userService.changePassword(userDTO.getId(), request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/{userId}/roles")
//    @AdminOnly
//    public ResponseEntity<Void> updateRoles(@PathVariable Long userId,
//                                            @RequestBody UpdateRolesRequest request) {
//        userService.updateUserRoles(userId, request);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping
//    @AdminOnly
//    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
//                                                             @RequestParam(defaultValue = "10") int size) {
//        Page<UserDTO> users = userService.getAllUsers(PageRequest.of(page, size));
//        return ResponseEntity.ok(users);
//    }
//}