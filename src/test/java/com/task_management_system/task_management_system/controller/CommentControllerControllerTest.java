package com.task_management_system.task_management_system.controller;

import com.task_management_system.task_management_system.model.dto.CommentRequestsDTO;
import com.task_management_system.task_management_system.model.dto.CommentResponse;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
class CommentControllerControllerTest extends BaseControllerTest {

    @Test
    void addComment_Success() throws Exception {
        Long taskId = 1L;
        CommentRequestsDTO commentRequest = new CommentRequestsDTO();
        commentRequest.setText("This is a sample comment.");

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(1L);
        commentResponse.setText("This is a sample comment.");
        commentResponse.setCreatedAt(null);

        when(commentService.addComment(anyLong(), any(CommentRequestsDTO.class), any(UserDTO.class))).thenReturn(commentResponse);

        String response = mockMvc.perform(post("/api/tasks/{taskId}/comments", taskId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new CommentRequestsDTO("gerge"))))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        log.info(response);
        mockMvc.perform(post("/api/tasks/{taskId}/comments", taskId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new CommentRequestsDTO("sfsdfs"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("This is a sample comment."));

    }

    @Test
    void deleteComment_Success() throws Exception {
        Long taskId = 1L;
        Long commentId = 1L;

        doNothing().when(commentService).deleteComment(anyLong(), anyLong(), any(UserDTO.class));

        mockMvc.perform(delete("/api/tasks/{taskId}/comments/{commentId}", taskId, commentId))
                .andExpect(status().isOk());
    }
}