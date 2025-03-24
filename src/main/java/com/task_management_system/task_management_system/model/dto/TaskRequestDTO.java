package com.task_management_system.task_management_system.model.dto;

import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object (DTO) for creating or updating a task. Includes task details such as " +
        "title, description, status, priority, and optional assignee email.")
public class TaskRequestDTO {
    @Schema(description = "Title of the task", example = "Fix bug in authentication module")
    private String title;

    @Schema(description = "Description of the task", example = "The bug occurs when the user logs in with an " +
            "invalid password")
    private String description;

    @Schema(description = "Status of the task", example = "IN_PROGRESS")
    private TaskStatus status;

    @Schema(description = "Priority of the task", example = "HIGH")
    private TaskPriority priority;

    @Schema(description = "Task assignee (optional). If not provided, the task will be assigned to the current user.",
            example = "user@user.com", nullable = true)
    @Email
    private String assigneeEmail;
}
