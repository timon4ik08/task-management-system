package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.model.*;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.repository.TaskRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private final ModelMapper mapper = new ModelMapper();

    public TaskResponseDTO createTask(TaskRequestDTO taskRequest, Long assigneeId, String assigneeEmail, UserDTO userDTO) {
//        User author = userRepository.findById(userDTO.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        User author = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User assignee = null;
        if(assigneeId != null && !assigneeId.equals(author.getId())){
            assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else if (assigneeEmail != null && !assigneeEmail.equals(author.getEmail())) {
            assignee = userRepository.findByEmail(assigneeEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(taskRequest.getPriority());
        task.setAuthor(author);
        task.setAssignee(assignee == null ? author : assignee);

        Task createdTask = taskRepository.save(task);

        return mapper.map(createdTask, TaskResponseDTO.class);
    }

    public Task assignTask(Long taskId, Long assigneeId, String assigneeEmail, UserDTO userDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User currentUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (!task.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("You can only assign your own tasks");
        }

        User assignee;
        if (assigneeId != null) {
            assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        } else if (assigneeEmail != null) {
            assignee = userRepository.findByEmail(assigneeEmail)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        } else {
            throw new IllegalArgumentException("Assignee ID or Email must be provided");
        }

        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if(!task.getAuthor().getId().equals(userDTO.getId()) || userDTO.isAdmin()){
            throw new RuntimeException("Not permision");
        }

        taskRepository.delete(task);
    }

    public Page<TaskResponseDTO> getMyTasks(UserDTO userDTO, Pageable pageable) {

        Page<Task> tasks = taskRepository.findByAssigneeId(userDTO.getId(), pageable);
        return tasks.map(task -> mapper.map(task, TaskResponseDTO.class));
    }

    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return tasks.map(task -> mapper.map(task, TaskResponseDTO.class));
    }

    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO taskRequest, UserDTO userDTO) { //Пользователи могут управлять своими задачами, если указаны как исполнитель: менять статус, оставлять комментарии.
        Task task = taskRepository.findById(taskId).orElseThrow( ()-> new RuntimeException("task none"));

        checkUpdatePermissions(task, userDTO, taskRequest);

        Optional.ofNullable(taskRequest.getTitle()).ifPresent(task::setTitle);
        Optional.ofNullable(taskRequest.getDescription()).ifPresent(task::setDescription);
        Optional.ofNullable(taskRequest.getStatus()).ifPresent(task::setStatus);
        Optional.ofNullable(taskRequest.getPriority()).ifPresent(task::setPriority);
        Optional.ofNullable(taskRequest.getAssignee()).ifPresent(task::setAssignee);

        Task taskUpdate = taskRepository.save(task);

        return mapper.map(taskUpdate, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO getTask(Long taskId, UserDTO userDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("none task"));
        boolean isAuthor = Objects.equals(task.getAuthor().getId(), userDTO.getId());
        boolean isAssignee = Objects.equals(task.getAssignee().getId(), userDTO.getId());
        boolean isAdmin = userDTO.isAdmin();

        if (!(isAuthor || isAssignee || isAdmin)) {
            throw new RuntimeException("No permission");
        }

        return mapper.map(task, TaskResponseDTO.class);
    }

    private void checkUpdatePermissions(Task task, UserDTO userDTO, TaskRequestDTO taskRequest) {
        boolean isAuthorOrAdmin = task.getAuthor().getId().equals(userDTO.getId()) || userDTO.isAdmin();

        if (!isAuthorOrAdmin && !task.getAssignee().getId().equals(userDTO.getId())) {
            throw new RuntimeException("You do not have permission to update this task.");
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
            if (taskRequest.getAssignee() != null) {
                unauthorizedFields.add("assignee");
            }

            if (!unauthorizedFields.isEmpty()) {
                throw new RuntimeException(String.valueOf(unauthorizedFields));
            }
        }
    }
}
