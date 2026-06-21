package project.training.com.example.demo.service.task;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse getTask(Long taskId);
    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);
    TaskResponse logTask(Long taskId, LogTaskRequest request);
}
