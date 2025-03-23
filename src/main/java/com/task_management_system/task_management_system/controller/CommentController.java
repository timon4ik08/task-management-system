package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.exception.response.ResponseException;
import com.task_management_system.task_management_system.model.dto.CommentRequestsDTO;
import com.task_management_system.task_management_system.model.dto.CommentResponse;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("api/tasks/{taskId}/comments")
@Tag(name = "Comment Management", description = "APIs for managing comments on tasks")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Operation(
            summary = "Add a comment to a task",
            description = "This endpoint allows users to add a comment to a specific task. The comment text is provided in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            )
    })
    public ResponseEntity<CommentResponse> addComment(
            @Parameter(description = "ID of the task to which the comment will be added", example = "1") @PathVariable Long taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Comment details",
                    content = @Content(
                            schema = @Schema(implementation = CommentRequestsDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"text\": \"This is a sample comment.\"}"
                            )
                    )
            )
            @RequestBody CommentRequestsDTO comment,
            @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Adding comment to task. Task ID: {}, User: {}, Comment: {}", taskId, userDTO, comment);
        CommentResponse commentResponse = commentService.addComment(taskId, comment, userDTO);
        log.debug("Comment added successfully: {}", commentResponse);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @Operation(
            summary = "Delete a comment from a task",
            description = "This endpoint allows users to delete a specific comment from a task. The task ID and comment ID are required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task or comment not found",
                    content = @Content(schema = @Schema(implementation = ResponseException.class))
            )
    })
    public ResponseEntity<CommentResponse> deleteComment(
            @Parameter(description = "ID of the task from which the comment will be deleted", example = "1") @PathVariable Long taskId,
            @Parameter(description = "ID of the comment to delete", example = "1") @PathVariable Long commentId,
            @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Deleting comment. Task ID: {}, Comment ID: {}, User: {}", taskId, commentId, userDTO);
        commentService.deleteComment(taskId, commentId, userDTO);
        log.debug("Comment deleted successfully. Task ID: {}, Comment ID: {}", taskId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}