package project.training.com.example.demo.service.task.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.exception.AppException;
import project.training.com.example.demo.exception.ErrorCode;
import project.training.com.example.demo.mapper.TaskMapper;
import project.training.com.example.demo.repository.TaskRepository;
import project.training.com.example.demo.service.task.TaskService;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @ServiceActivator(inputChannel = "CREATE_TASK_CHANNEL")
    public TaskResponse createTask(CreateTaskRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("CreateTaskRequest must not be null");
        }
        
        Task task = taskMapper.toEntity(request);
        
        // save DB
        Task saved = taskRepository.save(task);
        
        return taskMapper.toResponse(saved);
    }

    @Override
    @ServiceActivator(inputChannel = "GET_TASK_CHANNEL")
    public TaskResponse getTask(Long taskId) {
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        
        return taskMapper.toResponse(task);
    }
}
