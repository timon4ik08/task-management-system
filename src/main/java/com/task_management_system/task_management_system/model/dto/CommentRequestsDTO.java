package com.task_management_system.task_management_system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object (DTO) for creating or updating a comment. Contains the comment text.")
public class CommentRequestsDTO {
    @Schema(
            description = "Text content of the comment",
            example = "This is a sample comment.",
            required = true
    )
    private String text;
}
