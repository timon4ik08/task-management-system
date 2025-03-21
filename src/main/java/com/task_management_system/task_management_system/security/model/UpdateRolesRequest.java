package com.task_management_system.task_management_system.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Request object for updating user roles. Contains a set of roles to be assigned to the user.")
public class UpdateRolesRequest {
    @Schema(description = "Set of roles to assign to the user", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]", required = true)
    private Set<String> roles;
}