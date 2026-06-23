package project.training.com.example.demo.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.entity.TaskLog;
import project.training.com.example.demo.entity.TaskStatus;

@Component
public class TaskMapper {

    public TaskResponse toTaskResponse(Task task) {
        if (task == null) return null;

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus());
        response.setAssignee(task.getAssignee());
        response.setPoint(task.getPoint());
        response.setEstimateTime(task.getEstimateTime());
        response.setActualTime(task.getActualTime());
        response.setRemainingTime(task.getRemainingTime());
        response.setDateCreated(task.getDateCreated());
        response.setStartedAt(task.getStartedAt());
        response.setDeadline(task.getDeadline());

        return response;
    }

    public Task toTask(CreateTaskRequest request) {
        if (request == null) return null;

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
        
        return task;
    }

    public void toTask(UpdateTaskRequest request, Task task) {
        if (request == null || task == null) {
            return;
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        task.setAssignee(request.getAssignee());
        task.setPoint(request.getPoint());
        task.setEstimateTime(request.getEstimateTime());
    }

    public TaskLog toTaskLog(LogTaskRequest request) {

        if (request == null) {
            return null;
        }

        TaskLog taskLog = new TaskLog();
        taskLog.setLogDate(request.getDate());
        taskLog.setHours(request.getSpentHour());

        return taskLog;
    }
}
