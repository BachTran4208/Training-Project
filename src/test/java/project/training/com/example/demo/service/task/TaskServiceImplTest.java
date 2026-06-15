package project.training.com.example.demo.service.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.entity.Task;
import project.training.com.example.demo.entity.TaskStatus;
import project.training.com.example.demo.mapper.TaskMapper;
import project.training.com.example.demo.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_shouldSaveAndReturnResponse() {
        // given
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test task");
        request.setPoint(5);
        request.setEstimateTime(10);
        request.setAssignee("John");

        Task savedTask = new Task();
        savedTask.setTitle("Test task");

        TaskResponse response = new TaskResponse();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponse(savedTask)).thenReturn(response);

        // when
        TaskResponse result = taskService.createTask(request);

        // then
        assertNotNull(result);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        Task captured = taskCaptor.getValue();

        assertEquals("Test task", captured.getTitle());
        assertEquals(5, captured.getPoint());
        assertEquals(10, captured.getEstimateTime());
        assertEquals("John", captured.getAssignee());
        assertEquals(TaskStatus.IN_QUEUE, captured.getStatus());
        assertEquals(10, captured.getRemainingTime());
        assertEquals(0, captured.getActualTime());
        assertNotNull(captured.getDateCreated());
        assertNull(captured.getDeadline());

        verify(taskMapper).toResponse(savedTask);
    }

    @Test
    void createTask_shouldUseDefaultValues_whenNullFields() {
        // given
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task null test");
        request.setPoint(3);
        request.setEstimateTime(null);
        request.setAssignee(null);

        Task savedTask = new Task();
        TaskResponse response = new TaskResponse();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponse(savedTask)).thenReturn(response);

        // when
        taskService.createTask(request);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task task = captor.getValue();

        assertEquals("Undefined", task.getAssignee());
        assertEquals(0, task.getRemainingTime());
    }
}
