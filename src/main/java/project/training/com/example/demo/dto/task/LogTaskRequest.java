package project.training.com.example.demo.dto.task;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogTaskRequest {
    
    @NotNull
    private LocalDate date;

    @NotNull
    @Min(0)
    private Integer spentHour;
}