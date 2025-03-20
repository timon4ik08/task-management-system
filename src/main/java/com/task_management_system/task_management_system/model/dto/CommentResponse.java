package com.task_management_system.task_management_system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String text;
    private UserInfoDTO user;

    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime createdAt;
}
