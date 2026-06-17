package project.training.com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.training.com.example.demo.dto.RequestObject;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.gateway.TaskGateway;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskGateway taskGateway;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTask_success() throws Exception {

        // given
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("Test task");
        createTaskRequest.setPoint(3);
        createTaskRequest.setAssignee("John"); // optional
        createTaskRequest.setEstimateTime(2); // optional

        RequestObject<CreateTaskRequest> requestObject =
            RequestObject.<CreateTaskRequest>builder()
                    .transactionId("txn-123")
                    .serviceName("demo")
                    .data(createTaskRequest)
                    .build();

        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("Test task");

        // when
         when(taskGateway.createTask(any(CreateTaskRequest.class))).thenReturn(response);
        

        // then
        mockMvc.perform(post("/task")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestObject)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message")
                    .value("Create task successfully"))
            .andExpect(jsonPath("$.status")
                    .value(201))
            .andExpect(jsonPath("$.transactionId")
                    .value("txn-123"))
            .andExpect(jsonPath("$.serviceName")
                    .value("demo"))
            .andExpect(jsonPath("$.data.id")
                    .value(1))
            .andExpect(jsonPath("$.data.title")
                    .value("Test task"));

        verify(taskGateway, times(1)).createTask(any(CreateTaskRequest.class));
    }
}
