package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.model.dto.TaskResponseDTO;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {
    @GetMapping()
    public ResponseEntity<String> getTask(@Parameter(hidden = true)@CurrentUser UserDTO userDTO) {

        return new ResponseEntity<>(userDTO.getEmail() + userDTO.getRoles().toString(), HttpStatus.OK);
    }
}
