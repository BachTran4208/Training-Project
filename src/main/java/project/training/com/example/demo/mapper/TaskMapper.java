package project.training.com.example.demo.mapper;

import org.springframework.stereotype.Component;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.entity.Task;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
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
}
