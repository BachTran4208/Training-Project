package project.training.com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    RESOURCE_NOT_FOUND(404, "Resource not found"),
    INVALID_LOG_WORK(400, "Invalid log work"),
    RESOURCE_ALREADY_EXIST(409, "Resource already found"),
    UNAUTHORIZED(401, "Unauthorized"); 

    private final int code;
    private final String message;
}

