package com.task_management_system.task_management_system.model.dto;

import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import com.task_management_system.task_management_system.model.User;
import lombok.Data;

//@Schema(description = "DTO для создания задачи")
@Data
public class TaskRequestDTO {
    //@Schema(description = "Название задачи", example = "Исправить баг в коде")
    private String title;

    //@Schema(description = "Описание задачи", example = "Не работает кнопка логина")
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private User assignee;
}
