package project.training.com.example.demo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.training.com.example.demo.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private TaskStatus status;
    private String assignee;
    private Integer point;
    private Integer estimateTime;

    private LocalDate dateCreated;

    private LocalDateTime deadline;
    private LocalDateTime startedAt;

    private Integer actualTime;
    private Integer remainingTime;
}
