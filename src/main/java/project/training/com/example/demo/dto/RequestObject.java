package project.training.com.example.demo.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RequestObject<T> {
    
    @NotBlank(message = "TransactionId is required")
    private String transactionId;

    @NotBlank(message = "ServiceName is required")
    private String serviceName;

    private @Valid T data;
}
