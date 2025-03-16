//package com.task_management_system.task_management_system.controller;
//
//import com.task_management_system.task_management_system.model.dto.TaskCreateDTO;
//import com.task_management_system.task_management_system.model.dto.TaskDTO;
//import com.task_management_system.task_management_system.service.TaskService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("api/tasks")
//public class TaskController {
//
//    private final TaskService taskService;
//
//    @Autowired
//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    @PostMapping
//    ResponseEntity<?> createTask(@RequestBody TaskCreateDTO taskCreateDTO){
//        taskService.createTask(taskCreateDTO);
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    ResponseEntity<?> getTaskById(@PathVariable Long taskId){
//        TaskDTO taskDTO = taskService.getTaskById(taskId);
//        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
//    }
//}
