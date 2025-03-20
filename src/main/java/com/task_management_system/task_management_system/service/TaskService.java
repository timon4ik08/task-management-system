package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.model.*;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequest, Long assigneeId, String assigneeEmail, UserDTO userDTO);

    Task assignTask(Long taskId, Long assigneeId, String assigneeEmail, UserDTO userDTO);

    void deleteTask(Long taskId, UserDTO userDTO);

    Page<TaskResponseDTO> getMyTasks(UserDTO userDTO, Pageable pageable);

    Page<TaskResponseDTO> getAllTasks(Pageable pageable);

    TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest, UserDTO userDTO);

    TaskResponseDTO getTask(Long taskId, UserDTO userDTO);
}
