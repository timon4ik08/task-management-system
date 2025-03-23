package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.exception.TaskException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.model.*;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.repository.TaskRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        log.info("TaskServiceImpl initialized with dependencies");
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequest, UserDTO userDTO) {
        log.info("Attempting to create a new task by user with ID: {}", userDTO.getId());
        User author = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userDTO.getId());
                    return new UserException("User not found");
                });

        User assignee = null;
        if (taskRequest.getAssigneeEmail() != null && !taskRequest.getAssigneeEmail().equals(userDTO.getEmail())) {
            log.debug("Looking for assignee with email: {}", taskRequest.getAssigneeEmail());
            assignee = userRepository.findByEmail(taskRequest.getAssigneeEmail())
                    .orElseThrow(() -> {
                        log.error("Assignee not found with email: {}", taskRequest.getAssigneeEmail());
                        return new UserException("User not found");
                    });
        }

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(taskRequest.getPriority());
        task.setAuthor(author);
        task.setAssignee(assignee == null ? author : assignee);

        Task createdTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {}", createdTask.getId());
        return mapper.map(createdTask, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO assignTask(Long taskId, Long assigneeId, String assigneeEmail, UserDTO userDTO) {
        log.info("Attempting to assign task with ID: {} by user with ID: {}", taskId, userDTO.getId());
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", taskId);
                    return new TaskException("Task not found");
                });

        User currentUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> {
                    log.error("Current user not found with ID: {}", userDTO.getId());
                    return new UserException("Current user not found");
                });

        if (!task.getAuthor().equals(currentUser)) {
            log.warn("User with ID: {} attempted to assign a task they do not own", userDTO.getId());
            throw new AccessDeniedException("You can only assign your own tasks");
        }

        User assignee;
        if (assigneeId != null) {
            log.debug("Looking for assignee with ID: {}", assigneeId);
            assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> {
                        log.error("Assignee not found with ID: {}", assigneeId);
                        return new UserException("User for assignee not found");
                    });
        } else if (assigneeEmail != null) {
            log.debug("Looking for assignee with email: {}", assigneeEmail);
            assignee = userRepository.findByEmail(assigneeEmail)
                    .orElseThrow(() -> {
                        log.error("Assignee not found with email: {}", assigneeEmail);
                        return new UserException("User for assignee not found");
                    });
        } else {
            log.error("Assignee ID or Email not provided");
            throw new IllegalArgumentException("Assignee ID or Email must be provided");
        }

        task.setAssignee(assignee);
        Task taskUpdate = taskRepository.save(task);
        log.info("Task with ID: {} successfully assigned to user with ID: {}", taskId, assignee.getId());
        return mapper.map(taskUpdate, TaskResponseDTO.class);
    }

    @Override
    public void deleteTask(Long taskId, UserDTO userDTO) {
        log.info("Attempting to delete task with ID: {} by user with ID: {}", taskId, userDTO.getId());
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", taskId);
                    return new TaskException("Task not found");
                });

        if (!task.getAuthor().getId().equals(userDTO.getId()) && !userDTO.isAdmin()) {
            log.warn("User with ID: {} attempted to delete a task they do not own or are not admin", userDTO.getId());
            throw new AccessDeniedException("Only the author or admin can delete this task.");
        }

        taskRepository.delete(task);
        log.info("Task with ID: {} successfully deleted", taskId);
    }

    @Override
    public Page<TaskResponseDTO> getMyTasks(UserDTO userDTO, Pageable pageable) {
        log.info("Fetching tasks assigned to user with ID: {}", userDTO.getId());
        Page<Task> tasks = taskRepository.findByAssigneeId(userDTO.getId(), pageable);
        log.info("Successfully fetched {} tasks for user with ID: {}", tasks.getTotalElements(), userDTO.getId());
        return tasks.map(task -> mapper.map(task, TaskResponseDTO.class));
    }

    @Override
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        log.info("Fetching all tasks with pagination: {}", pageable);
        Page<Task> tasks = taskRepository.findAll(pageable);
        log.info("Successfully fetched {} tasks", tasks.getTotalElements());
        return tasks.map(task -> mapper.map(task, TaskResponseDTO.class));
    }

    @Override
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest, UserDTO userDTO) {
        log.info("Attempting to update task with ID: {} by user with ID: {}", taskId, userDTO.getId());
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", taskId);
                    return new TaskException("Task not found with id = " + taskId);
                });

        checkUpdatePermissions(task, userDTO, taskRequest);

        Optional.ofNullable(taskRequest.getTitle()).ifPresent(task::setTitle);
        Optional.ofNullable(taskRequest.getDescription()).ifPresent(task::setDescription);
        Optional.ofNullable(taskRequest.getStatus()).ifPresent(task::setStatus);
        Optional.ofNullable(taskRequest.getPriority()).ifPresent(task::setPriority);
        Optional.ofNullable(taskRequest.getAssigneeEmail())
                .ifPresent(email -> {
                    log.debug("Looking for assignee with email: {}", email);
                    userRepository.findByEmail(email)
                            .orElseThrow(() -> {
                                log.error("User not found for assignee with email: {}", email);
                                return new UserException("User not found for assignee with email = " + email);
                            });
                });

        Task taskUpdate = taskRepository.save(task);
        log.info("Task with ID: {} successfully updated", taskId);
        return mapper.map(taskUpdate, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO getTask(Long taskId, UserDTO userDTO) {
        log.info("Fetching task with ID: {} by user with ID: {}", taskId, userDTO.getId());
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", taskId);
                    return new TaskException("Task not found with id = " + taskId);
                });

        boolean isAuthor = Objects.equals(task.getAuthor().getId(), userDTO.getId());
        boolean isAssignee = Objects.equals(task.getAssignee().getId(), userDTO.getId());
        boolean isAdmin = userDTO.isAdmin();

        if (!(isAuthor || isAssignee || isAdmin)) {
            log.warn("User with ID: {} attempted to access a task they are not authorized to view", userDTO.getId());
            throw new AccessDeniedException("Only the author, admin or assigned user can get this task.");
        }

        log.info("Task with ID: {} successfully fetched by user with ID: {}", taskId, userDTO.getId());
        return mapper.map(task, TaskResponseDTO.class);
    }

    private void checkUpdatePermissions(Task task, UserDTO userDTO, TaskRequestDTO taskRequest) {
        boolean isAuthorOrAdmin = task.getAuthor().getId().equals(userDTO.getId()) || userDTO.isAdmin();

        if (!isAuthorOrAdmin && !task.getAssignee().getId().equals(userDTO.getId())) {
            log.warn("User with ID: {} attempted to edit a task they are not authorized to edit", userDTO.getId());
            throw new AccessDeniedException("Only the author, admin or assigned user can edit this task.");
        }

        if (!isAuthorOrAdmin) {
            Set<String> unauthorizedFields = new HashSet<>();

            if (taskRequest.getTitle() != null) {
                unauthorizedFields.add("title");
            }
            if (taskRequest.getDescription() != null) {
                unauthorizedFields.add("description");
            }
            if (taskRequest.getPriority() != null) {
                unauthorizedFields.add("priority");
            }
            if (taskRequest.getAssigneeEmail() != null) {
                unauthorizedFields.add("assigneeEmail");
            }

            if (!unauthorizedFields.isEmpty()) {
                log.warn("User with ID: {} attempted to edit unauthorized fields: {}", userDTO.getId(), unauthorizedFields);
                throw new AccessDeniedException("Only the author or admin can edit this field: " + unauthorizedFields);
            }
        }
    }
}
