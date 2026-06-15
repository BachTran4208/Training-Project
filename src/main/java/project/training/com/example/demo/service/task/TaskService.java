package project.training.com.example.demo.service.task;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);
}
