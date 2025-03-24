package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.model.filter.TaskFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequest, UserDTO userDTO);

    TaskResponseDTO assignTask(Long taskId, Long assigneeId, String assigneeEmail, UserDTO userDTO);

    void deleteTask(Long taskId, UserDTO userDTO);

    Page<TaskResponseDTO> getMyTasks(UserDTO userDTO, TaskFilter filter, Pageable pageable);

    Page<TaskResponseDTO> getAllTasks(TaskFilter filter , Pageable pageable);

    TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest, UserDTO userDTO);

    TaskResponseDTO getTask(Long taskId, UserDTO userDTO);
}
