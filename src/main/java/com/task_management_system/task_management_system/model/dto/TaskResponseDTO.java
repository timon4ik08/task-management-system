package com.task_management_system.task_management_system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Response Data Transfer Object (DTO) for a task. Contains full details about the task, including its title, description, status, priority, author, comment, create time, update time, assignee.")
public class TaskResponseDTO {
    @Schema(description = "ID of the task", example = "1")
    private Long id;

    @Schema(description = "Title of the task", example = "Fix bug in authentication module")
    private String title;

    @Schema(description = "Description of the task", example = "The bug occurs when the user logs in with an invalid password")
    private String description;

    @Schema(description = "Status of the task", example = "IN_PROGRESS")
    private TaskStatus status;

    @Schema(description = "Priority of the task", example = "HIGH")
    private TaskPriority priority;

    @Schema(description = "Task creator")
    private UserInfoDTO author;

    @Schema(description = "The task for whom it is assigned")
    private UserInfoDTO assignee;

    @Schema(description = "List of comments associated with the task. This field is excluded if the list is empty.",
            example = "[{\"id\": 1, \"text\": \"This is a sample comment.\", \"user\": {\"id\": 1, " +
                    "\"email\": \"user@example.com\", \"fullName\": \"John Doe\"}, " +
                    "\"createdAt\": \"21.03.2025 - 14:35\"}]")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentResponse> comments;

    @Schema(description = "Date and time when the task was created. Format: dd.MM.yyyy - HH:mm", example = "21.03.2025 - 14:30")
    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the task was last updated. Format: dd.MM.yyyy - HH:mm", example = "21.03.2025 - 15:45")
    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime updatedAt;

    @Schema(description = "Email of the user who last modified the task. This field is excluded if no modifications have been made.", example = "admin@example.com")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastModifiedBy;
}
