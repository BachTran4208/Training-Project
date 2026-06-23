package project.training.com.example.demo.service.task.impl;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.entity.TaskLog;
import project.training.com.example.demo.entity.TaskStatus;
import project.training.com.example.demo.exception.AppException;
import project.training.com.example.demo.exception.ErrorCode;
import project.training.com.example.demo.mapper.TaskMapper;
import project.training.com.example.demo.repository.TaskLogRepository;
import project.training.com.example.demo.repository.TaskRepository;
import project.training.com.example.demo.service.task.TaskService;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;
    private final TaskMapper taskMapper;

    @Override
    @ServiceActivator(inputChannel = "CREATE_TASK_CHANNEL")
    public TaskResponse createTask(CreateTaskRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("CreateTaskRequest must not be null");
        }
        
        Task task = taskMapper.toTask(request);
        
        // save DB
        Task saved = taskRepository.save(task);
        
        return taskMapper.toTaskResponse(saved);
    }

    @Override
    @ServiceActivator(inputChannel = "GET_TASK_CHANNEL")
    public TaskResponse getTask(Long taskId) {
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        
        return taskMapper.toTaskResponse(task);
    }

    @Override
    @ServiceActivator(inputChannel = "UPDATE_TASK_CHANNEL")
    public TaskResponse updateTask(@Header("taskId") Long taskId,
                                @Payload UpdateTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        TaskStatus oldStatus = task.getStatus();
        TaskStatus newStatus = request.getStatus();

        boolean isMovingToInProgress =
                newStatus == TaskStatus.IN_PROGRESS
                && oldStatus != TaskStatus.IN_PROGRESS;

        taskMapper.toTask(request, task);

        if (isMovingToInProgress && task.getStartedAt() == null) {
            task.setStartedAt(LocalDateTime.now());
        }

        if (task.getEstimateTime() != null && task.getStartedAt() != null) {
            task.setDeadline(task.getStartedAt().plusHours(task.getEstimateTime()));
        }

        Integer estimate = task.getEstimateTime();
        Integer actual = task.getActualTime();

        if (estimate == null) {
            task.setRemainingTime(null);
        } else if (actual == null) {
            task.setRemainingTime(estimate);
        } else {
            task.setRemainingTime(Math.max(estimate - actual, 0));
        }

        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }

    @Override
    @Transactional
    @ServiceActivator(inputChannel = "LOG_TASK_CHANNEL")
    public TaskResponse logTask(@Header("taskId") Long taskId, @Payload LogTaskRequest request) {
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (task.getStatus() != TaskStatus.IN_PROGRESS) 
            throw new AppException(ErrorCode.INVALID_LOG_WORK);

        // create log
        TaskLog taskLog = taskMapper.toTaskLog(request);
        taskLog.setTask(task);

        taskLogRepository.save(taskLog);
        
        // calculate actual time
        int totalHours = taskLogRepository.sumHoursByTaskId(taskId);
        task.setActualTime(totalHours);

        // calculate remaining time
        if (task.getEstimateTime() != null) {
            task.setRemainingTime(Math.max(task.getEstimateTime() - totalHours, 0));
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.toTaskResponse(savedTask);
    }
}
