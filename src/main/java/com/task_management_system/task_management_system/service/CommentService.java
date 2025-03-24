package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.model.dto.CommentRequestsDTO;
import com.task_management_system.task_management_system.model.dto.CommentResponse;
import com.task_management_system.task_management_system.model.dto.UserDTO;

public interface CommentService {
    CommentResponse addComment(Long taskId, CommentRequestsDTO comment, UserDTO userDTO);

    void deleteComment(Long taskId, Long commentId, UserDTO userDTO);
}
