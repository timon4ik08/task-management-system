package com.task_management_system.task_management_system.security.model;

import com.task_management_system.task_management_system.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@Schema(description = "Response object containing JWT token and user details after successful authentication.")
public class JwtResponse {
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    @Schema(description = "Type of the token", example = "Bearer", defaultValue = "Bearer")
    private String type = "Bearer";

    @Schema(description = "ID of the authenticated user", example = "1", required = true)
    private Long id;

    @Schema(description = "Email of the authenticated user", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Roles assigned to the authenticated user", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]", required = true)
    private Set<Role> roles;

    public JwtResponse(String accessToken, Long id, String email, Set<Role> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}