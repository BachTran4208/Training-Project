package project.training.com.example.demo.service.user.impl;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.entity.User;
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

        User user = userMapper.toUser(request);

        if (user.getRole() == null) {
            user.setRole(Role.OTHER);
        }

        User saved = userRepository.save(user);

        return userMapper.toUserResponse(saved);
    }
}
