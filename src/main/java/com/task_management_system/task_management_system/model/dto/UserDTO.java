package com.task_management_system.task_management_system.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.task_management_system.task_management_system.model.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Schema(description = "Data Transfer Object (DTO) representing user details, including ID, email, roles, and registration timestamp.")
public class UserDTO {
    @Schema(description = "Unique identifier of the user", example = "1", required = true)
    private Long id;

    @Schema(description = "Email address of the user", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "Set of roles assigned to the user", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]", required = true)
    private Set<String> roles = new HashSet<>();

    @Schema(description = "Timestamp of user registration in the format 'dd.MM.yyyy - HH:mm'", example = "01.01.2023 - 12:30", required = true)
    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime regAt;

    public boolean isAdmin() {
        return roles.stream().anyMatch(r -> r.equals(ERole.ROLE_ADMIN.name()));
    }
}
