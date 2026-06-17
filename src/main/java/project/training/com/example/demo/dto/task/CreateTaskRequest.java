package project.training.com.example.demo.dto.task;

import jakarta.validation.constraints.*;
import lombok.Data;
import project.training.com.example.demo.validation.annotation.ValidPoint;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be <= 100 characters")
    private String title;

    @Size(max = 50, message = "Assignee name too long")
    private String assignee;

    @ValidPoint(message = "Point must be one of: 1, 2, 3, 5, 8")
    private Integer point;

    @Min(value = 0, message = "Estimate time must be >= 0")
    private Integer estimateTime;
}
