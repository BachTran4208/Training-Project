package project.training.com.example.demo.mapper;

import org.junit.jupiter.api.Test;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.entity.TaskLog;
import project.training.com.example.demo.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void toResponse_shouldReturnNull_whenTaskIsNull() {
        // when
        TaskResponse response = taskMapper.toTaskResponse(null);

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
        TaskResponse response = taskMapper.toTaskResponse(task);

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

        Task task = taskMapper.toTask(request);

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

        Task task = taskMapper.toTask(request);

        assertEquals("Undefined", task.getAssignee());
        assertEquals(0, task.getRemainingTime());
    }

    @Test
    void toEntity_shouldReturnNull_whenRequestIsNull() {

        // when
        Task task = taskMapper.toTask(null);

        // then
        assertNull(task);
    }

    @Test
    void updateTask_shouldMapCorrectly() {

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated Task");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setAssignee("Jane");
        request.setPoint(8);
        request.setEstimateTime(12);

        Task task = new Task();
        task.setTitle("Old Task");

        taskMapper.toTask(request, task);

        assertEquals("Updated Task", task.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals("Jane", task.getAssignee());
        assertEquals(8, task.getPoint());
        assertEquals(12, task.getEstimateTime());
    }

    @Test
    void updateTask_shouldNotUpdateTitle_whenTitleIsNull() {

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle(null);
        request.setStatus(TaskStatus.DONE);

        Task task = new Task();
        task.setTitle("Original Title");

        taskMapper.toTask(request, task);

        assertEquals("Original Title", task.getTitle());
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void updateTask_shouldNotUpdateStatus_whenStatusIsNull() {

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("New Title");
        request.setStatus(null);

        Task task = new Task();
        task.setStatus(TaskStatus.IN_QUEUE);

        taskMapper.toTask(request, task);

        assertEquals(TaskStatus.IN_QUEUE, task.getStatus());
        assertEquals("New Title", task.getTitle());
    }

    @Test
    void updateTask_shouldReturn_whenRequestIsNull() {

        Task task = new Task();
        task.setTitle("Original");

        taskMapper.toTask(null, task);

        assertEquals("Original", task.getTitle());
    }

    @Test
    void updateTask_shouldReturn_whenTaskIsNull() {

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated");

        assertDoesNotThrow(() ->
                taskMapper.toTask(request, null));
    }

    @Test
    void toTaskLog_shouldReturnNull_whenRequestIsNull() {

        TaskLog taskLog = taskMapper.toTaskLog(null);

        assertNull(taskLog);
    }
}
