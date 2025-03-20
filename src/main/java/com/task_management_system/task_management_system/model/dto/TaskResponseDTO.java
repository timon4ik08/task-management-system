package com.task_management_system.task_management_system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserInfoDTO assignee;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentResponse> comments;

    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime updatedAt;

    private String lastModifiedBy;
}
