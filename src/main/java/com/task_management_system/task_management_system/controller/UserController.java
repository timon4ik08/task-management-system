package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.AdminOnly;
import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.exception.response.ResponseException;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.model.ChangePasswordRequest;
import com.task_management_system.task_management_system.security.model.UpdateRolesRequest;
import com.task_management_system.task_management_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "APIs for managing users, including retrieving user details," +
        " changing passwords, and updating roles.")
@RestController
@RequestMapping("api/users")
class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve detailed information about a user by their unique identifier. " +
                    "Only accessible by administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. Only administrators can access this endpoint.",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            )
    })
    @GetMapping("/{userId}")
    @AdminOnly
    public ResponseEntity<UserDTO> getUser(@Parameter(description = "ID of the user to retrieve", example = "1")
                                               @PathVariable Long userId) {
        UserDTO userDTO = userService.getCurrentUserId(userId);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(
            summary = "Change user password",
            description = "Allows the authenticated user to change their password. The current password and new password are required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            )
    })
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Parameter(hidden = true) @CurrentUser UserDTO userDTO,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                       description = "Password change request",
                                                       content = @Content(
                                                               schema = @Schema(implementation = ChangePasswordRequest.class),
                                                               examples = @ExampleObject(
                                                                       value = "{\"currentPassword\": \"oldPassword123\", \"newPassword\": \"newPassword123\"}"
                                                               )))
                                               @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userDTO.getId(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update user roles",
            description = "Allows administrators to update the roles of a user. The user ID and new roles are required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles updated successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. Only administrators can access this endpoint.",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            )
    })
    @PatchMapping("/{userId}/roles")
    @AdminOnly
    public ResponseEntity<UserDTO> updateRoles(@Parameter(description = "ID of the user whose roles will be updated", example = "1") @PathVariable Long userId,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                       description = "Roles update request",
                                                       content = @Content(
                                                               schema = @Schema(implementation = UpdateRolesRequest.class),
                                                               examples = @ExampleObject(
                                                                       value = "{\"roles\": [\"ROLE_ADMIN\", \"ROLE_USER\"]}"
                                                               )
                                                       )
                                               ) @RequestBody UpdateRolesRequest request) {
        UserDTO userDTO = userService.updateUserRoles(userId, request);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a paginated list of all users. Only accessible by administrators."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. Only administrators can access this endpoint.",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            )
    })
    @Parameters({
            @Parameter(
                    name = "page",
                    in = ParameterIn.QUERY,
                    description = "Page number (0-based)",
                    schema = @Schema(type = "integer", defaultValue = "0", minimum = "0")
            ),
            @Parameter(
                    name = "size",
                    in = ParameterIn.QUERY,
                    description = "Number of tasks per page",
                    schema = @Schema(type = "integer", defaultValue = "10", minimum = "1", maximum = "100")
            ),
            @Parameter(
                    name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property,asc|desc. Default sort order is ascending. Multiple sort criteria are supported.",
                    schema = @Schema(type = "array", example = "[\"id,asc\"]")
            )
    })
    @GetMapping
    @AdminOnly
    public ResponseEntity<Page<UserDTO>> getAllUsers(@Parameter(hidden = true) @PageableDefault(size = 5,
            sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
}