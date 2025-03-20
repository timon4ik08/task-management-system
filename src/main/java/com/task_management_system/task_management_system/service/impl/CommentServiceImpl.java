package com.task_management_system.task_management_system.service.impl;

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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    }

    @Override
    public CommentResponse addComment(Long taskId, CommentRequestsDTO commentRequests, UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("Not found user"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Not found task"));

        Comment comment = new Comment();
        comment.setText(commentRequests.getText());
        comment.setUser(user);
        comment.setTask(task);

        Comment commentSave = commentRepository.save(comment);
        return mapper.map(commentSave, CommentResponse.class);
    }

    @Override
    public void deleteComment(Long taskId, Long commentId, UserDTO userDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("not comment"));
        if(!Objects.equals(comment.getTask().getId(), taskId)){
            throw new RuntimeException("Not task with this id");
        }
        boolean isAuthor = Objects.equals(comment.getUser().getId(), userDTO.getId());
        boolean isAdmin = userDTO.isAdmin();

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }
}
