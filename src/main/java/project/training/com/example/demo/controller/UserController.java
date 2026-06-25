package project.training.com.example.demo.controller;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.training.com.example.demo.constants.AppConstants;
import project.training.com.example.demo.dto.ApiRequest;
import project.training.com.example.demo.dto.ApiResponse;
import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.LoginRequest;
import project.training.com.example.demo.dto.user.LoginResponse;
import project.training.com.example.demo.dto.user.UpdateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.gateway.UserGateway;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Value("${spring.application.name}")
    private String serviceName;

    private final UserGateway userGateway;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody ApiRequest<CreateUserRequest> requestObject) {

        MDC.put(AppConstants.TRANSACTION_ID, requestObject.getTransactionId());

        UserResponse user = userGateway.createUser(requestObject.getData());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("User created successfully")
                        .transactionId(requestObject.getTransactionId())
                        .serviceName(serviceName)
                        .data(user) 
                        .build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> editUser(
            @PathVariable Long userId,
            @Valid @RequestBody ApiRequest<UpdateUserRequest> requestObject,
            @AuthenticationPrincipal UserDetails userDetails) {

        UserResponse updatedUser = userGateway.updateUser(
                userId,
                userDetails.getUsername(),
                requestObject.getData()
        );

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("User updated successfully")
                        .transactionId(requestObject.getTransactionId())
                        .data(updatedUser)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody ApiRequest<LoginRequest> requestObject) {

        MDC.put(AppConstants.TRANSACTION_ID, requestObject.getTransactionId());

        LoginResponse response = userGateway.loginUser(requestObject.getData());

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Login successful")
                        .transactionId(requestObject.getTransactionId())
                        .data(response)
                        .build()
        );
    }

}
