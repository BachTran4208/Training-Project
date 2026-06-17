package project.training.com.example.demo.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Data is required")
    private @Valid T data;
}
