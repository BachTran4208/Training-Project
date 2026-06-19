package project.training.com.example.demo.exception;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import project.training.com.example.demo.constants.AppConstants;
import project.training.com.example.demo.dto.ApiRequest;
import project.training.com.example.demo.dto.ApiResponse;

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

        ApiRequest<?> requestObject =
            (ApiRequest<?>) ex.getBindingResult()
                    .getTarget();

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
            .message("Validation failed")
            .status(HttpStatus.BAD_REQUEST.value())
            .transactionId(requestObject != null ? requestObject.getTransactionId() : null)
            .serviceName(serviceName)
            .data(errors)
            .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        String transactionId = MDC.get(AppConstants.TRANSACTION_ID);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .message("Malformed JSON request")
            .status(HttpStatus.BAD_REQUEST.value())
            .transactionId(transactionId)
            .serviceName(serviceName)
            .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String transactionId = MDC.get(AppConstants.TRANSACTION_ID);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .message("Type mismatch error")
            .status(HttpStatus.BAD_REQUEST.value())
            .transactionId(transactionId)
            .serviceName(serviceName)
            .data(null)
            .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex) {

        String transactionId = MDC.get(AppConstants.TRANSACTION_ID);

        ApiResponse<String> response = ApiResponse.<String>builder()
            .message(ex.getErrorCode().getMessage())
            .status(ex.getErrorCode().getCode())
            .transactionId(transactionId)
            .serviceName(serviceName)
            .data(ex.getCause() != null ? ex.getCause().getMessage() : null)
            .build();

        return ResponseEntity.status(ex.getErrorCode().getCode()).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex) {

        String transactionId = MDC.get(AppConstants.TRANSACTION_ID);

        ApiResponse<String> response = ApiResponse.<String>builder()
            .message("An unexpected error occurred")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .transactionId(transactionId)
            .serviceName(serviceName)
            .data(ex.getMessage())
            .build();

        return ResponseEntity.internalServerError().body(response);
    }
}
