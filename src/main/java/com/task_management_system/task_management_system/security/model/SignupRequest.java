package com.task_management_system.task_management_system.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Schema(description = "Request object for user registration. Contains email and password for creating a new account.")
public class SignupRequest {
    @Schema(description = "Email of the user", example = "user@example.com", required = true)
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Schema(description = "Password of the user. Must be between 6 and 40 characters.", example = "password123", required = true)
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
