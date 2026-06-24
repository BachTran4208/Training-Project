package project.training.com.example.demo.service.user;

import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.LoginRequest;
import project.training.com.example.demo.dto.user.LoginResponse;
import project.training.com.example.demo.dto.user.UpdateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Long userId, String currentUserEmail, UpdateUserRequest request);
    LoginResponse login(LoginRequest request);
}
