package com.task_management_system.task_management_system.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request to change a user's password. Includes the old password for verification and the new password.")
public class ChangePasswordRequest {
    @Schema(description = "The user's current password", example = "oldPassword123", required = true)
    private String oldPassword;

    @Schema(description = "The new password to set", example = "newPassword456", required = true)
    private String newPassword;
}