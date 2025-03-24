package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.model.filter.TaskFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskControllerControllerTest extends BaseControllerTest {
    @Test
    void createTask_Success() throws Exception {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        when(taskService.createTask(any(TaskRequestDTO.class), any(UserDTO.class))).thenReturn(taskResponse);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void assignTask_Success() throws Exception {
        Long taskId = 1L;
        Long assigneeId = 2L;
        String assigneeEmail = "assignee@example.com";
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        when(taskService.assignTask(anyLong(), anyLong(), anyString(), any(UserDTO.class))).thenReturn(taskResponse);

        mockMvc.perform(put("/api/tasks/{taskId}/assign", taskId)
                        .param("assigneeId", assigneeId.toString())
                        .param("assigneeEmail", assigneeEmail))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_Success() throws Exception {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(anyLong(), any(UserDTO.class));

        mockMvc.perform(delete("/api/tasks/{taskId}", taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTask_Success() throws Exception {
        Long taskId = 1L;
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        when(taskService.getTask(anyLong(), any(UserDTO.class))).thenReturn(taskResponse);

        mockMvc.perform(get("/api/tasks/{taskId}", taskId))
                .andExpect(status().isOk());
    }

    @Test
    void getMyTasks_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        List<TaskResponseDTO> taskList = Collections.singletonList(taskResponse);
        Page<TaskResponseDTO> taskPage = new PageImpl<>(taskList, pageable, taskList.size());
        when(taskService.getMyTasks(any(UserDTO.class), any(TaskFilter.class), any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(get("/api/tasks/my")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "id,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTasks_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        List<TaskResponseDTO> taskList = Collections.singletonList(taskResponse);
        Page<TaskResponseDTO> taskPage = new PageImpl<>(taskList, pageable, taskList.size());

        when(taskService.getAllTasks(any(TaskFilter.class), any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void updateTask_Success() throws Exception {
        Long taskId = 1L;
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        when(taskService.updateTask(anyLong(), any(TaskRequestDTO.class), any(UserDTO.class))).thenReturn(taskResponse);

        mockMvc.perform(put("/api/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk());
    }
}