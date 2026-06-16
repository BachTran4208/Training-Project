package project.training.com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseObject {
    @JsonProperty("message")
    private String message;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("status")
    private int status;
    
    @JsonProperty("data")
    private Object data;
}
