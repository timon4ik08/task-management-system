package com.task_management_system.task_management_system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Response object representing a comment, including its ID, text, author, and creation timestamp.")
public class CommentResponse {
    @Schema(description = "Unique identifier of the comment", example = "1")
    private Long id;

    @Schema(description = "Text content of the comment", example = "This is a sample comment.")
    private String text;

    @Schema(description = "User who created the comment")
    private UserInfoDTO user;

    @Schema(description = "Date and time when the comment was created. Format: dd.MM.yyyy - HH:mm", example = "21.03.2025 - 14:35")
    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime createdAt;
}
