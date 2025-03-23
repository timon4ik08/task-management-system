package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.exception.CommentException;
import com.task_management_system.task_management_system.exception.TaskException;
import com.task_management_system.task_management_system.exception.UserException;
import com.task_management_system.task_management_system.model.Comment;
import com.task_management_system.task_management_system.model.Task;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.model.dto.CommentRequestsDTO;
import com.task_management_system.task_management_system.model.dto.CommentResponse;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.repository.CommentRepository;
import com.task_management_system.task_management_system.repository.TaskRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        log.info("CommentServiceImpl initialized with dependencies");
    }

    @Override
    public CommentResponse addComment(Long taskId, CommentRequestsDTO commentRequests, UserDTO userDTO) {
        log.info("Attempting to add a comment to task with ID: {} by user with ID: {}", taskId, userDTO.getId());
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userDTO.getId());
                    return new UserException("Not found user");
                });

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", taskId);
                    return new TaskException("Not found task with id = " + taskId);
                });

        Comment comment = new Comment();
        comment.setText(commentRequests.getText());
        comment.setUser(user);
        comment.setTask(task);

        Comment commentSave = commentRepository.save(comment);
        log.info("Comment successfully added with ID: {}", commentSave.getId());
        return mapper.map(commentSave, CommentResponse.class);
    }

    @Override
    public void deleteComment(Long taskId, Long commentId, UserDTO userDTO) {
        log.info("Attempting to delete comment with ID: {} from task with ID: {} by user with ID: {}", commentId, taskId, userDTO.getId());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: {}", commentId);
                    return new CommentException("Not found this comment with id = " + commentId);
                });

        if (!Objects.equals(comment.getTask().getId(), taskId)) {
            log.error("Comment with ID: {} does not belong to task with ID: {}", commentId, taskId);
            throw new CommentException("Not found task with comment with id = " + commentId);
        }

        boolean isAuthor = Objects.equals(comment.getUser().getId(), userDTO.getId());
        boolean isAdmin = userDTO.isAdmin();

        if (!isAuthor && !isAdmin) {
            log.warn("User with ID: {} attempted to delete a comment they do not own or are not admin", userDTO.getId());
            throw new AccessDeniedException("You do not have permission to delete this comment");
        }

        commentRepository.deleteById(commentId);
        log.info("Comment with ID: {} successfully deleted", commentId);
    }
}
