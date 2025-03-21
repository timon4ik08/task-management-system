package com.task_management_system.task_management_system.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response object containing a message, typically used for success or informational responses.")
public class MessageResponse {
    @Schema(description = "The message to be returned", example = "Registration successful!", required = true)
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}