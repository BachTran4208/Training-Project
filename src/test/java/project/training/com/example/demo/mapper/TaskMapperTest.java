package project.training.com.example.demo.mapper;

import org.junit.jupiter.api.Test;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void toResponse_shouldReturnNull_whenTaskIsNull() {
        // when
        TaskResponse response = taskMapper.toResponse(null);

        // then
        assertNull(response);
    }

    @Test
    void toResponse_shouldMapCorrectly() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setAssignee("John");
        task.setPoint(5);
        task.setEstimateTime(10);
        task.setActualTime(2);
        task.setRemainingTime(8);
        task.setDateCreated(LocalDate.now());
        task.setDeadline(LocalDateTime.now().plusHours(10));

        // when
        TaskResponse response = taskMapper.toResponse(task);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test task", response.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        assertEquals("John", response.getAssignee());
        assertEquals(5, response.getPoint());
        assertEquals(10, response.getEstimateTime());
        assertEquals(2, response.getActualTime());
        assertEquals(8, response.getRemainingTime());
    }

}
