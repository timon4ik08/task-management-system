package com.task_management_system.task_management_system.model.filter;

import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Filter criteria for task search")
public class TaskFilter {

    @Schema(
            description = "Filter by task title (case-insensitive partial match)",
            example = "Important task"
    )
    private String title;

    @Schema(
            description = "Filter by task status",
            example = "IN_PROGRESS",
            allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"}
    )
    private TaskStatus status;

    @Schema(
            description = "Filter by task priority",
            example = "HIGH",
            allowableValues = {"LOW", "MEDIUM", "HIGH"}
    )
    private TaskPriority priority;

    @Schema(
            description = "Filter by author ID",
            example = "1"
    )
    private Long authorId;

    @Schema(
            description = "Filter by author email",
            example = "manager@company.com"
    )
    private String authorEmail;

    @Schema(
            description = "Filter by assignee ID",
            example = "2"
    )
    private Long assigneeId;

    @Schema(
            description = "Filter by assignee email",
            example = "developer@company.com"
    )
    private String assigneeEmail;

    @Schema(
            description = "Filter by minimum creation date (inclusive)",
            example = "2023-01-01T00:00:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime createdAt;
}