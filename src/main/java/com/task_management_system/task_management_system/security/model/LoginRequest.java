package com.task_management_system.task_management_system.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for user login. Contains email and password for authentication.")
public class LoginRequest {
    @Schema(description = "Email of the user", example = "user@example.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Password of the user", example = "password123", required = true)
    @NotBlank
    private String password;
}
