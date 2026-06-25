package project.training.com.example.demo.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.entity.User;
import project.training.com.example.demo.exception.AppException;
import project.training.com.example.demo.exception.ErrorCode;
import project.training.com.example.demo.repository.UserRepository;
import project.training.com.example.demo.service.user.impl.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // given
        String email = "test@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setRole(Role.MEMBER);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        // when
        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals(1, result.getAuthorities().size());
        verify(userRepository, times(1)).findByEmail(email);
        
    }

    @Test
    void loadUserByUsername_shouldThrowAppException_whenUserNotFound() {
        // given
        String email = "notfound@gmail.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // when & then
        AppException ex = assertThrows(
                AppException.class,
                () -> customUserDetailsService.loadUserByUsername(email)
        );

        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());

        verify(userRepository, times(1)).findByEmail(email);
    }
}
