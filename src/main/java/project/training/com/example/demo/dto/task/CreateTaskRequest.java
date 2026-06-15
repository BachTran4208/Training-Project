package project.training.com.example.demo.dto.task;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be <= 100 characters")
    private String title;

    @Size(max = 50, message = "Assignee name too long")
    private String assignee;

    @NotNull(message = "Point is required")
    @Min(value = 1, message = "Point must be >= 1")
    @Max(value = 8, message = "Point must be <= 8")
    private Integer point;

    @Min(value = 0, message = "Estimate time must be >= 0")
    private Integer estimateTime;
}
