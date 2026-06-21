package project.training.com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    // Default: IN_QUEUE
    private TaskStatus status;

    // Default: Undefined
    private String assignee;

    private Integer point;

    // hour
    private Integer estimateTime;

    private LocalDate dateCreated;

    // IN_PROGRESS
    private LocalDateTime startedAt;

    // deadline = startedAt + estimateTime
    private LocalDateTime deadline;

    private Integer actualTime;
    private Integer remainingTime;

    @OneToMany(mappedBy = "task")
    private List<TaskLog> logs;
}
