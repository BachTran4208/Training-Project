package project.training.com.example.demo.service.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import project.training.com.example.demo.service.task.impl.TaskServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskLogRepository taskLogRepository;

    private TaskMapper taskMapper = new TaskMapper();

    private TaskServiceImpl taskService;


    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository, taskLogRepository , taskMapper);
    }

    @Test
    void createTask_shouldSaveAndReturnResponse() {

        // given
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test task");
        request.setPoint(5);
        request.setEstimateTime(10);
        request.setAssignee("John");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test task");
        savedTask.setPoint(5);
        savedTask.setEstimateTime(10);
        savedTask.setAssignee("John");

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

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

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
    
        // when
        TaskResponse result = taskService.createTask(request);

        // then
        assertNotNull(result);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task task = captor.getValue();

        assertEquals("Undefined", task.getAssignee());
        assertEquals(0, task.getRemainingTime());
    }

    @Test
    void createTask_shouldThrowException_whenRequestIsNull() {

        // when + then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(null)
        );

        assertEquals("CreateTaskRequest must not be null", ex.getMessage());

        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTask_shouldReturnTaskResponse_whenTaskExists() {

        // given
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test task");
        task.setPoint(5);
        task.setEstimateTime(10);
        task.setAssignee("John");
        task.setStatus(TaskStatus.IN_QUEUE);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        // when
        TaskResponse result = taskService.getTask(taskId);

        // then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Test task", result.getTitle());
        assertEquals(5, result.getPoint());
        assertEquals(10, result.getEstimateTime());
        assertEquals("John", result.getAssignee());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void getTask_shouldThrowException_whenTaskNotFound() {

        // given
        Long taskId = 999L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        // when + then
        AppException ex = assertThrows(
                AppException.class,
                () -> taskService.getTask(taskId)
        );

        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotFound() {

        // given
        Long taskId = 999L;
        UpdateTaskRequest request = new UpdateTaskRequest();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        // when + then
        AppException ex = assertThrows(
                AppException.class,
                () -> taskService.updateTask(taskId, request)
        );

        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void updateTask_shouldUpdateBasicFields() {

        // given
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Old title");
        task.setAssignee("John");
        task.setPoint(3);
        task.setEstimateTime(10);
        task.setActualTime(2);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("New title");
        request.setAssignee("Mike");
        request.setPoint(5);
        request.setEstimateTime(20);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TaskResponse response = taskService.updateTask(taskId, request);

        // then
        assertNotNull(response);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task saved = captor.getValue();

        assertEquals("New title", saved.getTitle());
        assertEquals("Mike", saved.getAssignee());
        assertEquals(5, saved.getPoint());
        assertEquals(20, saved.getEstimateTime());

        assertEquals(18, saved.getRemainingTime());
    }

    @Test
    void updateTask_shouldSetStartedAtAndDeadline_whenMoveToInProgressFirstTime() {

        // given
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_QUEUE);
        task.setEstimateTime(8);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setEstimateTime(7);
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPoint(5);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.updateTask(taskId, request);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task saved = captor.getValue();

        assertEquals(TaskStatus.IN_PROGRESS, saved.getStatus());

        assertNotNull(saved.getStartedAt());
        assertNotNull(saved.getDeadline());

        assertEquals(
                saved.getStartedAt().plusHours(request.getEstimateTime()),
                saved.getDeadline()
        );
    }

    @Test
    void updateTask_shouldNotOverrideStartedAt_whenAlreadyStarted() {

        // given
        Long taskId = 1L;

        LocalDateTime startedAt = LocalDateTime.now().minusHours(2);

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setStartedAt(startedAt);
        task.setEstimateTime(5);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setStatus(TaskStatus.IN_QUEUE);
        request.setEstimateTime(5);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.updateTask(taskId, request);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task saved = captor.getValue();

        assertEquals(startedAt, saved.getStartedAt());
    }

    @Test
    void updateTask_shouldRecalculateDeadline_whenEstimateTimeChanges() {

        // given
        Long taskId = 1L;

        LocalDateTime startedAt = LocalDateTime.of(2025, 1, 1, 10, 0);

        Task task = new Task();
        task.setId(taskId);
        task.setStartedAt(startedAt);
        task.setEstimateTime(10);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setEstimateTime(20);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.updateTask(taskId, request);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task saved = captor.getValue();

        assertEquals(
                startedAt.plusHours(20),
                saved.getDeadline()
        );
    }

    @Test
    void logTask_shouldThrowException_whenTaskNotFound() {

        Long taskId = 999L;

        LogTaskRequest request = new LogTaskRequest();
        request.setDate(LocalDate.now());
        request.setSpentHour(2);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        AppException ex = assertThrows(
                AppException.class,
                () -> taskService.logTask(taskId, request)
        );

        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void logTask_shouldThrowException_whenTaskNotInProgress() {

        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_QUEUE);

        LogTaskRequest request = new LogTaskRequest();
        request.setDate(LocalDate.now());
        request.setSpentHour(2);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        AppException ex = assertThrows(
                AppException.class,
                () -> taskService.logTask(taskId, request)
        );

        assertEquals(ErrorCode.INVALID_LOG_WORK, ex.getErrorCode());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void logTask_shouldCreateLogAndUpdateActualTime() {

        // given
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setEstimateTime(10);

        LogTaskRequest request = new LogTaskRequest();
        request.setDate(LocalDate.now());
        request.setSpentHour(3);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskLogRepository.sumHoursByTaskId(taskId))
                .thenReturn(4);

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TaskResponse response = taskService.logTask(taskId, request);

        // then
        assertNotNull(response);

        ArgumentCaptor<TaskLog> logCaptor =
                ArgumentCaptor.forClass(TaskLog.class);

        verify(taskLogRepository).save(logCaptor.capture());

        TaskLog savedLog = logCaptor.getValue();

        assertEquals(task, savedLog.getTask());
        assertEquals(request.getDate(), savedLog.getLogDate());
        assertEquals(request.getSpentHour(), savedLog.getHours());

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();

        assertEquals(4, savedTask.getActualTime());
        assertEquals(6, savedTask.getRemainingTime());
    }

    @Test
    void logTask_shouldNotCalculateRemainingTime_whenEstimateTimeIsNull() {

        // given
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setEstimateTime(null);

        LogTaskRequest request = new LogTaskRequest();
        request.setDate(LocalDate.now());
        request.setSpentHour(2);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskLogRepository.sumHoursByTaskId(taskId))
                .thenReturn(5);

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TaskResponse response = taskService.logTask(taskId, request);

        // then
        assertNotNull(response);

        ArgumentCaptor<TaskLog> logCaptor =
                ArgumentCaptor.forClass(TaskLog.class);

        verify(taskLogRepository).save(logCaptor.capture());

        TaskLog savedLog = logCaptor.getValue();

        assertEquals(request.getDate(), savedLog.getLogDate());
        assertEquals(request.getSpentHour(), savedLog.getHours());

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();

        assertEquals(5, savedTask.getActualTime());

        // estimateTime == null nên block này không chạy
        // remainingTime giữ nguyên giá trị hiện tại (thường là null)
        assertNull(savedTask.getRemainingTime());
    }
}
