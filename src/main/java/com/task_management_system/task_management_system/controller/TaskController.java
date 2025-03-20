package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.AdminOnly;
import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest,
                                                      @RequestParam(required = false) Long assigneeId,
                                                      @RequestParam(required = false) String assigneeEmail,
                                                      @CurrentUser UserDTO userDTO) {
        TaskResponseDTO task = taskService.createTask(taskRequest, assigneeId, assigneeEmail, userDTO);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}/assign")
    public ResponseEntity<Task> assignTask( //Todo
            @PathVariable Long taskId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String assigneeEmail,
            @CurrentUser UserDTO userDTO) {
        Task task = taskService.assignTask(taskId, assigneeId, assigneeEmail, userDTO);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @CurrentUser UserDTO userDTO) {
        taskService.deleteTask(taskId, userDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long taskId,
                                                   @CurrentUser UserDTO userDTO) {
        TaskResponseDTO task = taskService.getTask(taskId, userDTO);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<TaskResponseDTO>> getMyTasks(@CurrentUser UserDTO userDTO,
                                                            @PageableDefault(size = 5,
                                                                    sort = "id",
                                                                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.getMyTasks(userDTO, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/all")
    @AdminOnly
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(@PageableDefault(size = 5,
            sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.getAllTasks(pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskRequest,
            @CurrentUser UserDTO userDTO) {
        TaskResponseDTO taskUpdate = taskService.updateTask(taskId, taskRequest, userDTO);
        return new ResponseEntity<>(taskUpdate, HttpStatus.OK);
    }
}
