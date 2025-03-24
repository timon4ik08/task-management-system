package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.AdminOnly;
import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.exception.response.ResponseException;
import com.task_management_system.task_management_system.model.TaskPriority;
import com.task_management_system.task_management_system.model.TaskStatus;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.TaskRequestDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.model.filter.TaskFilter;
import com.task_management_system.task_management_system.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Task Management", description = "APIs for managing tasks in the system")
@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Create a new task",
            description = "This endpoint creates a new task in the system. The task is associated with the current authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest,
                                                      @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Creating a new task. Request: {}, User: {}", taskRequest, userDTO);
        TaskResponseDTO task = taskService.createTask(taskRequest, userDTO);
        log.debug("Task created successfully: {}", task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Assign a task",
            description = "This endpoint assigns a task to a user by their ID or email. The task must exist, and the assignee must be a valid user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "404", description = "Task or assignee not found", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponseDTO> assignTask(
            @PathVariable Long taskId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String assigneeEmail,
            @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Assigning task. Task ID: {}, Assignee ID: {}, Assignee Email: {}, User: {}", taskId, assigneeId, assigneeEmail, userDTO);
        TaskResponseDTO task = taskService.assignTask(taskId, assigneeId, assigneeEmail, userDTO);
        log.debug("Task assigned successfully: {}", task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a task",
            description = "This endpoint deletes a task by its ID. Only the task owner or an admin can delete the task."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId,
                                           @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Deleting task. Task ID: {}, User: {}", taskId, userDTO);
        taskService.deleteTask(taskId, userDTO);
        log.debug("Task deleted successfully. Task ID: {}", taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Get a task by ID",
            description = "This endpoint retrieves a task by its ID. Only the task owner or an admin can access the task."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long taskId,
                                                   @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Fetching task. Task ID: {}, User: {}", taskId, userDTO);
        TaskResponseDTO task = taskService.getTask(taskId, userDTO);
        log.debug("Task fetched successfully: {}", task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Operation(
            summary = "Get tasks of the current user",
            description = "This endpoint retrieves a paginated list of tasks assigned to the current authenticated user.",
            parameters = {
                    @Parameter(
                            name = "title",
                            description = "Filter by task title (case-insensitive partial match)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", example = "urgent")
                    ),
                    @Parameter(
                            name = "status",
                            description = "Filter by task status",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = TaskStatus.class, example = "IN_PROGRESS")
                    ),
                    @Parameter(
                            name = "priority",
                            description = "Filter by task priority",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = TaskPriority.class, example = "HIGH")
                    ),
                    @Parameter(
                            name = "authorId",
                            description = "Filter by author ID",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", format = "int64", example = "1")
                    ),
                    @Parameter(
                            name = "authorEmail",
                            description = "Filter by author email",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "email", example = "manager@example.com")
                    ),
                    @Parameter(
                            name = "createdAt",
                            description = "Filter by minimum creation date (inclusive)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "date-time", example = "2023-01-01T00:00:00")
                    ),
                    @Parameter(
                            name = "page",
                            description = "Page number (0-based)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Page size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "5")
                    ),
                    @Parameter(
                            name = "sort",
                            description = "Sorting criteria (format: property,asc|desc)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", defaultValue = "id,desc")
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @GetMapping("/my")
    public ResponseEntity<Page<TaskResponseDTO>> getMyTasks(@ModelAttribute TaskFilter filter,
                                                            @Parameter(hidden = true) @CurrentUser UserDTO userDTO,
                                                            @PageableDefault(size = 5,
                                                                    sort = "id",
                                                                    direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching tasks for user. User: {}, Pageable: {}", userDTO, pageable);
        Page<TaskResponseDTO> tasks = taskService.getMyTasks(userDTO, filter, pageable);
        log.debug("Tasks fetched successfully: {}", tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all tasks with filtering and pagination",
            description = "Retrieves a paginated list of tasks with optional filtering. Requires ADMIN role.",
            parameters = {
                    @Parameter(
                            name = "title",
                            description = "Filter by task title (partial match)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "status",
                            description = "Filter by task status",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = TaskStatus.class)
                    ),
                    @Parameter(
                            name = "priority",
                            description = "Filter by task priority",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = TaskPriority.class)
                    ),
                    @Parameter(
                            name = "authorId",
                            description = "Filter by author ID",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", format = "int64")
                    ),
                    @Parameter(
                            name = "authorEmail",
                            description = "Filter by author email",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "email")
                    ),
                    @Parameter(
                            name = "assigneeId",
                            description = "Filter by assignee ID",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", format = "int64")
                    ),
                    @Parameter(
                            name = "assigneeEmail",
                            description = "Filter by assignee email",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "email")
                    ),
                    @Parameter(
                            name = "createdAt",
                            description = "Filter by minimum creation date",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", format = "date-time")
                    ),
                    @Parameter(
                            name = "page",
                            description = "Page number (0-based)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Page size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "5")
                    ),
                    @Parameter(
                            name = "sort",
                            description = "Sorting criteria (format: property,asc|desc)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", defaultValue = "id,desc")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved tasks",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - authentication required"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - requires ADMIN role"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request - invalid filter parameters"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/all")
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(
            @ModelAttribute TaskFilter filter,
            @Parameter(hidden = true) @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching all tasks. Pageable: {}", pageable);
        Page<TaskResponseDTO> tasks = taskService.getAllTasks(filter, pageable);
        log.debug("All tasks fetched successfully: {}", tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a task",
            description = "This endpoint updates an existing task by its ID. Only the task owner or an admin can update the task."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskRequest,
            @Parameter(hidden = true) @CurrentUser UserDTO userDTO) {
        log.info("Updating task. Task ID: {}, Request: {}, User: {}", taskId, taskRequest, userDTO);
        TaskResponseDTO taskUpdate = taskService.updateTask(taskId, taskRequest, userDTO);
        log.debug("Task updated successfully: {}", taskUpdate);
        return new ResponseEntity<>(taskUpdate, HttpStatus.OK);
    }
}
