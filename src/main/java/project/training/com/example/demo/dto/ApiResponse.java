package project.training.com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private int status;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("data")
    private T data;
}
