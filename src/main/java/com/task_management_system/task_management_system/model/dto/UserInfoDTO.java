package com.task_management_system.task_management_system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response Data Transfer Object (DTO) for a user. Contains full details about the user.")
public class UserInfoDTO {
    @Schema(description = "Unique identifier of the user", example = "1")
    Long id;

    @Schema(description = "Email of the user", example = "user@example.com")
    String email;
}
