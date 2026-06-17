package project.training.com.example.demo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project.training.com.example.demo.controller.TaskController;
import project.training.com.example.demo.dto.RequestObject;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.gateway.TaskGateway;
import tools.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(GlobalHandleException.class)
public class GlobalHandleExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskGateway taskGateway;

    private RequestObject<CreateTaskRequest> wrap(CreateTaskRequest request) {

        return RequestObject.<CreateTaskRequest>builder()
                .transactionId("txn-123")
                .serviceName("task-service")
                .data(request)
                .build();
    }

    @Test
    void shouldFail_whenTitleIsBlank() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(""); // invalid
        request.setPoint(1);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data['data.title']")
                        .value("Title is required"));
    }

    @Test
    void shouldFail_whenTitleTooLong() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("a".repeat(101));
        request.setPoint(1);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data['data.title']")
                        .value("Title must be <= 100 characters"));
    }

    @Test
    void shouldFail_whenAssigneeTooLong() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");
        request.setAssignee("a".repeat(51));
        request.setPoint(1);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data['data.assignee']")
                        .value("Assignee name too long"));
    }

    @Test
    void shouldFail_whenPointNull() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");
        request.setPoint(null);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data['data.point']").value("Point must be one of: 1, 2, 3, 5, 8"));
    }

    @Test
    void shouldFail_whenPointNotInAllowedList() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");
        request.setPoint(4);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data['data.point']")
                        .value("Point must be one of: 1, 2, 3, 5, 8"));
    }

    @Test
    void shouldFail_whenEstimateTimeNegative() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");
        request.setPoint(3);
        request.setEstimateTime(-1);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(jsonPath("$.data['data.estimateTime']").value("Estimate time must be >= 0"));
    }

    @Test
    void handleValidation_multipleFieldErrors() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");     // error 1
        request.setPoint(null);   // error 2

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data['data.title']").exists())
                .andExpect(jsonPath("$.data['data.point']").exists());
    }

    @Test
    void handleValidation_emptyBody() throws Exception {

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateTask_success() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");
        request.setPoint(3);

        TaskResponse response = new TaskResponse();

        when(taskGateway.createTask(any())).thenReturn(response);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrap(request))))
                .andExpect(status().isCreated());

        verify(taskGateway).createTask(any());
    }
}
