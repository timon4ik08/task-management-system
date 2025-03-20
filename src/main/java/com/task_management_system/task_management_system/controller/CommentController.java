package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.model.dto.CommentRequestsDTO;
import com.task_management_system.task_management_system.model.dto.CommentResponse;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long taskId,
                                                      @RequestBody CommentRequestsDTO comment,
                                                      @CurrentUser UserDTO userDTO){
        CommentResponse commentResponse = commentService.addComment(taskId, comment, userDTO);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long taskId,
                                                         @PathVariable Long commentId,
                                                         @CurrentUser UserDTO userDTO){
        commentService.deleteComment(taskId, commentId, userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
