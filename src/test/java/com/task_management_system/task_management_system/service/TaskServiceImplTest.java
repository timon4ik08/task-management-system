package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.exception.TaskException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest extends BaseServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;
    private User author;
    private User assignee;
    private UserDTO userDTO;
    private Task task;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);
        author.setEmail("author@example.com");

        assignee = new User();
        assignee.setId(2L);
        assignee.setEmail("assignee@example.com");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("author@example.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setAuthor(author);
        task.setAssignee(assignee);
    }

    @Test
    void createTask_ShouldCreateTaskSuccessfully() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("New Task Description");
        taskRequest.setPriority(TaskPriority.HIGH);
        taskRequest.setAssigneeEmail("assignee@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(userRepository.findByEmail("assignee@example.com")).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO response = taskService.createTask(taskRequest, userDTO);

        assertNotNull(response);
        assertEquals("Test Task", response.getTitle());
    }

    @Test
    void createTask_ShouldThrowException_WhenAssigneeNotFound() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setAssigneeEmail("nonexistent@example.com");

        lenient().when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());


        assertThrows(UserException.class, () -> taskService.createTask(taskRequest, userDTO));
    }

    @Test
    void assignTask_ShouldThrowException_WhenNoAssigneeIdOrEmailProvided() {
        UserDTO authorDTO = new UserDTO();
        authorDTO.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        UserException exception = assertThrows(UserException.class,
                () -> taskService.assignTask(1L, null, null, authorDTO));

        assertEquals("Current user not found", exception.getMessage());
    }

    @Test
    void assignTask_ShouldThrowException_WhenAssigneeEmailNotFound() {
        UserDTO authorDTO = new UserDTO();
        authorDTO.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        UserException exception = assertThrows(UserException.class,
                () -> taskService.assignTask(1L, null, "notfound@example.com", authorDTO));

        assertEquals("Current user not found", exception.getMessage());
    }

    @Test
    void getAllTasks_ShouldReturnEmptyPage_WhenNoTasksExist() {
        Page<Task> emptyPage = Page.empty();
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<TaskResponseDTO> result = taskService.getAllTasks(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }


    @Test
    void assignTask_ShouldAssignTaskSuccessfully() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO response = taskService.assignTask(1L, 2L, null, userDTO);

        assertNotNull(response);
        assertEquals("Test Task", response.getTitle());
    }

    @Test
    void assignTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskException.class, () -> taskService.assignTask(1L, 2L, null, userDTO));
    }

    @Test
    void deleteTask_ShouldThrowException_WhenUserNotAllowed() {
        UserDTO unauthorizedUser = new UserDTO();
        unauthorizedUser.setId(3L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.deleteTask(1L, unauthorizedUser));
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskException.class, () -> taskService.deleteTask(1L, userDTO));
    }

    @Test
    void getMyTasks_ShouldReturnTasksForUser() {
        List<Task> taskList = List.of(task);
        Page<Task> taskPage = new PageImpl<>(taskList);

        when(taskRepository.findByAssigneeId(1L, PageRequest.of(0, 10))).thenReturn(taskPage);

        Page<TaskResponseDTO> result = taskService.getMyTasks(userDTO, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateTask_ShouldUpdateTaskSuccessfully() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("Updated Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO response = taskService.updateTask(1L, taskRequest, userDTO);

        assertNotNull(response);
        assertEquals("Updated Task", response.getTitle());
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskException.class, () -> taskService.updateTask(1L, taskRequest, userDTO));
    }

    @Test
    void getTask_ShouldReturnTask_WhenUserHasAccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO response = taskService.getTask(1L, userDTO);

        assertNotNull(response);
        assertEquals("Test Task", response.getTitle());
    }

    @Test
    void getTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskException.class, () -> taskService.getTask(1L, userDTO));
    }

    @Test
    void assignTask_ShouldThrowException_WhenUserIsNotAuthor() {
        UserDTO anotherUserDTO = new UserDTO();
        anotherUserDTO.setId(3L);
        anotherUserDTO.setEmail("another@example.com");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(3L)).thenReturn(Optional.of(assignee));

        assertThrows(AccessDeniedException.class, () -> taskService.assignTask(1L, 2L, null, anotherUserDTO));
    }

    @Test
    void updateTask_ShouldThrowException_WhenUserNotAuthorized() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("New Title");
        taskRequest.setDescription("New Description");
        taskRequest.setPriority(TaskPriority.HIGH);
        taskRequest.setAssigneeEmail("newEmail@email.com");

        UserDTO unauthorizedUser = new UserDTO();
        unauthorizedUser.setId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> taskService.updateTask(1L, taskRequest, unauthorizedUser));
        assertEquals("Only the author or admin can edit this field: [description, assigneeEmail, title, priority]", exception.getMessage());
    }

    @Test
    void updateTask_ShouldThrowException_WhenUserNotAuthorizedMessage() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("New Title");

        UserDTO unauthorizedUser = new UserDTO();
        unauthorizedUser.setId(3L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> taskService.updateTask(1L, taskRequest, unauthorizedUser));

        assertEquals("Only the author, admin or assigned user can edit this task.", exception.getMessage());
    }

}
