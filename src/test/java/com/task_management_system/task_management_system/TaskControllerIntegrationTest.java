package com.task_management_system.task_management_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void createTask_IntegrationTest() throws Exception {
//        String taskRequest = "{\"title\": \"Test Task\", \"description\": \"Test Description\"}";
//
//        mockMvc.perform(post("/api/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(taskRequest))
//                .andExpect(status().isCreated());
//    }
}