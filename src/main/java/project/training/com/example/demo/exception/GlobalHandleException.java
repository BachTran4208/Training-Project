package project.training.com.example.demo.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.training.com.example.demo.dto.RequestObject;
import project.training.com.example.demo.dto.ResponseObject;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleException {

    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        RequestObject<?> requestObject =
            (RequestObject<?>) ex.getBindingResult()
                    .getTarget();

        ResponseObject response = ResponseObject.builder()
            .message("Validation failed")
            .status(HttpStatus.BAD_REQUEST.value())
            .transactionId(requestObject.getTransactionId() != null ? requestObject.getTransactionId() : null)
            .serviceName(serviceName)
            .data(errors)
            .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex) {

        ResponseObject response = ResponseObject.builder()
            .message(ex.getMessage())
            .status(ex.getErrorCode().getCode())
            .serviceName(serviceName)
            .data(ex.getCause() != null ? ex.getCause().getMessage() : null)
            .build();

        return ResponseEntity.status(ex.getErrorCode().getCode()).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex) {

        ResponseObject response = ResponseObject.builder()
            .message("An unexpected error occurred")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .serviceName(serviceName)
            .data(ex.getMessage())
            .build();

        return ResponseEntity.internalServerError().body(response);
    }
}
