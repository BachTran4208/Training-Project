package project.training.com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.training.com.example.demo.dto.ApiRequest;
import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.gateway.UserGateway;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserGateway userGateway;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_success() throws Exception {

        CreateUserRequest req = new CreateUserRequest();
        req.setName("John Doe");
        req.setDob("01-01-2001");
        req.setEmail("john@example.com");
        req.setPhone("0912345678");
        req.setOffice("HCM");
        req.setRole(null);

        ApiRequest<CreateUserRequest> requestObject =
                ApiRequest.<CreateUserRequest>builder()
                        .transactionId("txn-001")
                        .data(req)
                        .build();

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("John Doe");

        when(userGateway.createUser(any(CreateUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.transactionId").value("txn-001"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"));

        verify(userGateway, times(1))
                .createUser(any(CreateUserRequest.class));
    }

    @Test
    void createUser_missingRequiredFields_shouldFail() throws Exception {

        CreateUserRequest req = new CreateUserRequest();
        // bỏ trống name + office

        ApiRequest<CreateUserRequest> requestObject =
                ApiRequest.<CreateUserRequest>builder()
                        .transactionId("txn-002")
                        .data(req)
                        .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isBadRequest());

        verify(userGateway, times(0))
                .createUser(any(CreateUserRequest.class));
    }

    @Test
    void createUser_invalidEmail_shouldFail() throws Exception {

        CreateUserRequest req = new CreateUserRequest();
        req.setName("John");
        req.setOffice("HCM");
        req.setEmail("invalid-email"); // sai format

        ApiRequest<CreateUserRequest> requestObject =
                ApiRequest.<CreateUserRequest>builder()
                        .transactionId("txn-003")
                        .data(req)
                        .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isBadRequest());

        verify(userGateway, times(0))
                .createUser(any(CreateUserRequest.class));
    }

    @Test
    void createUser_invalidPhone_shouldFail() throws Exception {

        CreateUserRequest req = new CreateUserRequest();
        req.setName("John");
        req.setOffice("HCM");
        req.setPhone("abc123"); // invalid phone

        ApiRequest<CreateUserRequest> requestObject =
                ApiRequest.<CreateUserRequest>builder()
                        .transactionId("txn-004")
                        .data(req)
                        .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isBadRequest());

        verify(userGateway, times(0))
                .createUser(any(CreateUserRequest.class));
    }

    @Test
    void createUser_invalidDob_shouldFail() throws Exception {

        CreateUserRequest req = new CreateUserRequest();
        req.setName("John");
        req.setOffice("HCM");
        req.setDob("31-31-2020"); // invalid date

        ApiRequest<CreateUserRequest> requestObject =
                ApiRequest.<CreateUserRequest>builder()
                        .transactionId("txn-005")
                        .data(req)
                        .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isBadRequest());

        verify(userGateway, times(0))
                .createUser(any(CreateUserRequest.class));
    }
}
