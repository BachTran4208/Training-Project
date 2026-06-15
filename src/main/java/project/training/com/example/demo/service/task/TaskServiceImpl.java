package project.training.com.example.demo.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.entity.TaskStatus;
import project.training.com.example.demo.mapper.TaskMapper;
import project.training.com.example.demo.repository.TaskRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @ServiceActivator(inputChannel = "CREATE_TASK_CHANNEL")
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setPoint(request.getPoint());
        task.setEstimateTime(request.getEstimateTime());

        task.setStatus(TaskStatus.IN_QUEUE);
        task.setAssignee(
                request.getAssignee() != null ? request.getAssignee() : "Undefined"
        );

        task.setDateCreated(LocalDate.now());

        task.setActualTime(0);
        task.setRemainingTime(
                request.getEstimateTime() != null ? request.getEstimateTime() : 0
        );

        // deadline = undefined when create
        task.setDeadline(null);

        // save DB
        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }
}
