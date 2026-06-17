package project.training.com.example.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.training.com.example.demo.dto.RequestObject;
import project.training.com.example.demo.dto.ResponseObject;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.gateway.TaskGateway;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    @Value("${spring.application.name}")
    private String serviceName;

    private final TaskGateway taskGateway;

    @PostMapping
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody RequestObject<CreateTaskRequest> requestObject) {

        CreateTaskRequest createTaskRequest = requestObject.getData();

        TaskResponse task = taskGateway.createTask(createTaskRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseObject.builder()
                        .message("Create task successfully")
                        .status(HttpStatus.CREATED.value())
                        .transactionId(requestObject.getTransactionId())
                        .serviceName(serviceName)
                        .data(task)
                        .build());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseObject> get(@Valid @PathVariable Long taskId, @Valid RequestObject<Void> requestObject) {

        TaskResponse task = taskGateway.getTask(taskId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseObject.builder()
                        .message("Task found")
                        .status(HttpStatus.OK.value())
                        .transactionId(requestObject.getTransactionId())
                        .serviceName(serviceName)
                        .data(task)
                        .build());
    }
}
