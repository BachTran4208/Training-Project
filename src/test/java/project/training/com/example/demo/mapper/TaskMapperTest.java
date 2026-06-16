package project.training.com.example.demo.mapper;

import org.junit.jupiter.api.Test;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
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

    @Test
    void toEntity_shouldMapCorrectly() {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test task");
        request.setPoint(5);
        request.setEstimateTime(10);
        request.setAssignee("John");

        Task task = taskMapper.toEntity(request);

        assertNotNull(task);
        assertEquals("Test task", task.getTitle());
        assertEquals(5, task.getPoint());
        assertEquals(10, task.getEstimateTime());
        assertEquals("John", task.getAssignee());

        assertEquals(TaskStatus.IN_QUEUE, task.getStatus());
        assertEquals(10, task.getRemainingTime());
        assertEquals(0, task.getActualTime());
        assertNull(task.getDeadline());
        assertNotNull(task.getDateCreated());
    }

    @Test
    void toEntity_shouldHandleNullFields() {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test");
        request.setEstimateTime(null);
        request.setAssignee(null);

        Task task = taskMapper.toEntity(request);

        assertEquals("Undefined", task.getAssignee());
        assertEquals(0, task.getRemainingTime());
    }

    @Test
    void toEntity_shouldReturnNull_whenRequestIsNull() {

        // when
        Task task = taskMapper.toEntity(null);

        // then
        assertNull(task);
    }

}
