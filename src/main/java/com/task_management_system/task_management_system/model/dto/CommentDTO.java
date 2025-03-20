package com.task_management_system.task_management_system.model.dto;

import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String text;
    private UserInfoDTO user;
    private LocalDateTime createdAt;
}