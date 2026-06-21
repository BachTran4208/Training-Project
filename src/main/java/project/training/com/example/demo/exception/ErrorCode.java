package project.training.com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    RESOURCE_NOT_FOUND(404, "Resource not found"),
    INVALID_LOG_WORK(400, "Invalid log work");

    private final int code;
    private final String message;
}

