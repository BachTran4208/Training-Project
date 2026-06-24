package project.training.com.example.demo.service.user.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import project.training.com.example.demo.entity.User;
import project.training.com.example.demo.exception.AppException;
import project.training.com.example.demo.exception.ErrorCode;
import project.training.com.example.demo.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password("") 
                .authorities(user.getRole().name())
                .build();
    }
}