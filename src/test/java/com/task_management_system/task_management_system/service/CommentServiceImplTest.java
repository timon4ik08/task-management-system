package com.task_management_system.task_management_system.service;

import com.task_management_system.task_management_system.exception.CommentException;
import com.task_management_system.task_management_system.exception.TaskException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.model.Comment;
import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.model.dto.CommentRequestsDTO;
import com.task_management_system.task_management_system.model.dto.CommentResponse;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest extends BaseServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private User admin;
    private UserDTO userDTO;
    private UserDTO adminDTO;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        admin = new User();
        admin.setId(2L);
        admin.setEmail("admin@example.com");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");

        adminDTO = new UserDTO();
        adminDTO.setId(2L);
        adminDTO.setEmail("admin@example.com");


        task = new Task();
        task.setId(100L);

        comment = new Comment();
        comment.setId(10L);
        comment.setText("Sample comment");
        comment.setUser(user);
        comment.setTask(task);
    }

    @Test
    void addComment_ShouldAddCommentSuccessfully() {
        CommentRequestsDTO request = new CommentRequestsDTO();
        request.setText("New comment");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse response = commentService.addComment(100L, request, userDTO);

        assertNotNull(response);
        assertEquals("Sample comment", response.getText());
    }

    @Test
    void addComment_ShouldThrowException_WhenUserNotFound() {
        CommentRequestsDTO request = new CommentRequestsDTO();
        request.setText("New comment");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> commentService.addComment(100L, request, userDTO));
        assertEquals("Not found user", exception.getMessage());
    }

    @Test
    void addComment_ShouldThrowException_WhenTaskNotFound() {
        CommentRequestsDTO request = new CommentRequestsDTO();
        request.setText("New comment");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findById(100L)).thenReturn(Optional.empty());

        TaskException exception = assertThrows(TaskException.class, () -> commentService.addComment(100L, request, userDTO));
        assertEquals("Not found task with id = 100", exception.getMessage());
    }

    @Test
    void deleteComment_ShouldDeleteCommentSuccessfully_WhenUserIsAuthor() {
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.deleteComment(100L, 10L, userDTO));
        verify(commentRepository).deleteById(10L);
    }

    @Test
    void deleteComment_ShouldDeleteCommentSuccessfully_WhenUserIsAdmin() {
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.deleteComment(100L, 10L, userDTO));
        verify(commentRepository).deleteById(10L);
    }

    @Test
    void deleteComment_ShouldThrowException_WhenCommentNotFound() {
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        CommentException exception = assertThrows(CommentException.class, () -> commentService.deleteComment(10L, 10L, userDTO));
        assertEquals("Not found this comment with id = 10", exception.getMessage());
    }

    @Test
    void deleteComment_ShouldThrowException_WhenTaskMismatch() {
        Task anotherTask = new Task();
        anotherTask.setId(200L);
        comment.setTask(anotherTask);

        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        CommentException exception = assertThrows(CommentException.class, () -> commentService.deleteComment(100L, 10L, userDTO));
        assertEquals("Not found task with comment with id = 10", exception.getMessage());
    }

    @Test
    void deleteComment_ShouldThrowException_WhenUserHasNoPermission() {
        UserDTO anotherUserDTO = new UserDTO();
        anotherUserDTO.setId(3L);
        anotherUserDTO.setEmail("another@example.com");

        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> commentService.deleteComment(100L, 10L, anotherUserDTO));
        assertEquals("You do not have permission to delete this comment", exception.getMessage());
    }
}