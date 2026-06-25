package project.training.com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.training.com.example.demo.dto.ApiRequest;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;
import project.training.com.example.demo.gateway.TaskGateway;
import project.training.com.example.demo.security.JwtService;
import project.training.com.example.demo.service.user.impl.CustomUserDetailsService;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskGateway taskGateway;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createTask_success() throws Exception {

        // given
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("Test task");
        createTaskRequest.setPoint(3);
        createTaskRequest.setAssignee("John"); // optional
        createTaskRequest.setEstimateTime(2); // optional

        ApiRequest<CreateTaskRequest> requestObject =
            ApiRequest.<CreateTaskRequest>builder()
                    .transactionId("txn-123")
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

    @Test
    void getTask_success() throws Exception {

        // given
        Long taskId = 1L;

        String transactionId = "txn-456";

        TaskResponse response = new TaskResponse();
        response.setId(taskId);
        response.setTitle("Test task");

        when(taskGateway.getTask(taskId)).thenReturn(response);

        // when + then
        mockMvc.perform(get("/task/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Transaction-Id", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Task found"))
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.transactionId")
                        .value("txn-456"))
                .andExpect(jsonPath("$.serviceName")
                        .value("demo"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.title")
                        .value("Test task"));

        verify(taskGateway, times(1)).getTask(taskId);
    }

    @Test
    void updateTask_success() throws Exception {

        // given
        Long taskId = 1L;

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle("Updated task");
        updateTaskRequest.setPoint(5);
        updateTaskRequest.setAssignee("Jane");
        updateTaskRequest.setEstimateTime(4);

        ApiRequest<UpdateTaskRequest> requestObject =
                ApiRequest.<UpdateTaskRequest>builder()
                        .transactionId("txn-789")
                        .data(updateTaskRequest)
                        .build();

        TaskResponse response = new TaskResponse();
        response.setId(taskId);
        response.setTitle("Updated task");

        when(taskGateway.updateTask(eq(taskId), any(UpdateTaskRequest.class)))
                .thenReturn(response);

        // when + then
        mockMvc.perform(patch("/task/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Task updated successfully"))
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.transactionId")
                        .value("txn-789"))
                .andExpect(jsonPath("$.serviceName")
                        .value("demo"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.title")
                        .value("Updated task"));

        verify(taskGateway, times(1))
                .updateTask(eq(taskId), any(UpdateTaskRequest.class));
    }

    @Test
    void logTask_success() throws Exception {

        // given
        Long taskId = 1L;

        LogTaskRequest logTaskRequest = new LogTaskRequest();
        logTaskRequest.setDate(LocalDate.of(2025, 1, 15));
        logTaskRequest.setSpentHour(4);

        ApiRequest<LogTaskRequest> requestObject =
                ApiRequest.<LogTaskRequest>builder()
                        .transactionId("txn-999")
                        .data(logTaskRequest)
                        .build();

        TaskResponse response = new TaskResponse();
        response.setId(taskId);
        response.setTitle("Test task");

        when(taskGateway.logTask(eq(taskId), any(LogTaskRequest.class)))
                .thenReturn(response);

        // when + then
        mockMvc.perform(post("/task/{taskId}/logs", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Task updated successfully"))
                .andExpect(jsonPath("$.status")
                        .value(200))
                .andExpect(jsonPath("$.transactionId")
                        .value("txn-999"))
                .andExpect(jsonPath("$.serviceName")
                        .value("demo"))
                .andExpect(jsonPath("$.data.id")
                        .value(1))
                .andExpect(jsonPath("$.data.title")
                        .value("Test task"));

        verify(taskGateway, times(1))
                .logTask(eq(taskId), any(LogTaskRequest.class));
    }

    @Test
    void logTask_validationFailed() throws Exception {
        ApiRequest<LogTaskRequest> requestObject =
                ApiRequest.<LogTaskRequest>builder()
                        .transactionId("txn-999")
                        .data(new LogTaskRequest()) // thiếu field required
                        .build();

        mockMvc.perform(post("/task/{taskId}/logs", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isBadRequest());

        verify(taskGateway, never())
                .logTask(anyLong(), any(LogTaskRequest.class));
    }
}
