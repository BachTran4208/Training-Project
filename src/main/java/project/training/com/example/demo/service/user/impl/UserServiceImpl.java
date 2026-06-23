package project.training.com.example.demo.service.user.impl;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UpdateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.entity.User;
import project.training.com.example.demo.exception.AppException;
import project.training.com.example.demo.exception.ErrorCode;
import project.training.com.example.demo.mapper.UserMapper;
import project.training.com.example.demo.repository.UserRepository;
import project.training.com.example.demo.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @ServiceActivator(inputChannel = "CREATE_USER_CHANNEL")
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXIST);
        }

        User user = userMapper.toUser(request);

        if (user.getRole() == null) {
            user.setRole(Role.OTHER);
        }

        User saved = userRepository.save(user);

        return userMapper.toUserResponse(saved);
    }

    @Override
    @ServiceActivator(inputChannel = "UPDATE_USER_CHANNEL")
    public UserResponse updateUser(@Header Long userId, @Header String currentUserEmail, @Payload UpdateUserRequest request) {

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_ALREADY_EXIST));

        boolean isOwner = targetUser.getEmail()
                .equalsIgnoreCase(currentUserEmail);

        boolean isScrumOrPO =
                currentUser.getRole() == Role.SCRUM_MASTER ||
                currentUser.getRole() == Role.PROJECT_OWNER;

        if (!isOwner && !isScrumOrPO) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        boolean isTryingChangeRole = request.getRole() != null;
        boolean isRestrictedRole =
            targetUser.getRole() == Role.MEMBER ||
            targetUser.getRole() == Role.OTHER;
        if (isOwner && isRestrictedRole && isTryingChangeRole) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        userMapper.updateUser(targetUser, request);

        User updatedUser = userRepository.save(targetUser);

        return userMapper.toUserResponse(updatedUser);
    }
}
