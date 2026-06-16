package project.training.com.example.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody CreateTaskRequest request) {

        TaskResponse task = taskGateway.createTask(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseObject.builder()
                        .message("Create task successfully")
                        .status(HttpStatus.CREATED.value())
                        .data(task)
                        .build());
    }
}
