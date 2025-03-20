package com.task_management_system.task_management_system.model.dto;

import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import com.task_management_system.task_management_system.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private User assignee;
}
