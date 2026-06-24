package project.training.com.example.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.training.com.example.demo.constants.AppConstants;
import project.training.com.example.demo.dto.ApiRequest;
import project.training.com.example.demo.dto.ApiResponse;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;
import project.training.com.example.demo.gateway.TaskGateway;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    @Value("${spring.application.name}")
    private String serviceName;

    private final TaskGateway taskGateway;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> create(@Valid @RequestBody ApiRequest<CreateTaskRequest> requestObject) {

        MDC.put(AppConstants.TRANSACTION_ID, requestObject.getTransactionId());

        System.out.println("👉 HIT CREATE TASK CONTROLLER");
        
        CreateTaskRequest createTaskRequest = requestObject.getData();

        TaskResponse task = taskGateway.createTask(createTaskRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TaskResponse>builder()
                        .message("Create task successfully")
                        .status(HttpStatus.CREATED.value())
                        .transactionId(requestObject.getTransactionId())
                        .serviceName(serviceName)
                        .data(task)
                        .build());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> get(
        @Valid @PathVariable Long taskId,
        @RequestHeader("X-Transaction-Id") String transactionId
    ) {

        MDC.put(AppConstants.TRANSACTION_ID, transactionId);

        TaskResponse task = taskGateway.getTask(taskId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TaskResponse>builder()
                        .message("Task found")
                        .status(HttpStatus.OK.value())
                        .transactionId(transactionId)
                        .serviceName(serviceName)
                        .data(task)
                        .build());
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> update(
            @Valid @PathVariable Long taskId,
            @Valid @RequestBody ApiRequest<UpdateTaskRequest> requestObject
    ) {
        
        MDC.put(AppConstants.TRANSACTION_ID, requestObject.getTransactionId());

        UpdateTaskRequest updateTaskRequest = requestObject.getData();
        TaskResponse task = taskGateway.updateTask(taskId, updateTaskRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TaskResponse>builder()
                        .message("Task updated successfully")
                        .status(HttpStatus.OK.value())
                        .transactionId(requestObject.getTransactionId())
                        .serviceName(serviceName)
                        .data(task)
                        .build());
    }

    @PostMapping("/{taskId}/logs")
    public ResponseEntity<ApiResponse<TaskResponse>> logTask(
            @Valid @PathVariable Long taskId,
            @Valid @RequestBody ApiRequest<LogTaskRequest> requestObject
    ) {
        
        MDC.put(AppConstants.TRANSACTION_ID, requestObject.getTransactionId());

        LogTaskRequest logTaskRequest = requestObject.getData();
        TaskResponse task = taskGateway.logTask(taskId, logTaskRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TaskResponse>builder()
                        .message("Task updated successfully")
                        .status(HttpStatus.OK.value())
                        .transactionId(requestObject.getTransactionId())
                        .serviceName(serviceName)
                        .data(task)
                        .build());
    }
}
