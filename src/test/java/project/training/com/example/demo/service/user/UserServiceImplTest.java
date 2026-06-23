package project.training.com.example.demo.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.entity.User;
import project.training.com.example.demo.mapper.UserMapper;
import project.training.com.example.demo.repository.UserRepository;
import project.training.com.example.demo.service.user.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserMapper userMapper = new UserMapper();

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void createUser_shouldSaveAndReturnResponse() {

        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John");
        request.setDob("01-15-2000");
        request.setEmail("JOHN@GMAIL.COM");
        request.setPhone("1234567890");
        request.setOffice("HCM");
        request.setRole(Role.MEMBER);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        // when
        UserResponse response = userService.createUser(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getName());
        assertEquals("011500", response.getDob()); // MMddyy
        assertEquals("1234 567 890", response.getPhone()); // formatted
        assertEquals("john@gmail.com", response.getEmail()); // lower case
        assertEquals("HCM", response.getOffice());
        assertEquals(Role.MEMBER.toString(), response.getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldSetDefaultRole_whenRoleIsNull() {

        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John");
        request.setDob("01-15-2000");
        request.setEmail("john@gmail.com");
        request.setPhone("1234567890");
        request.setOffice("HCM");
        request.setRole(null);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        userService.createUser(request);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();

        assertEquals(Role.OTHER, saved.getRole());
    }

    @Test
    void createUser_shouldLowercaseEmail_andConvertDob() {

        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Test");
        request.setDob("12-31-1999");
        request.setEmail("TEST@EMAIL.COM");
        request.setPhone("0987654321");
        request.setOffice("HN");
        request.setRole(Role.MEMBER);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResponse response = userService.createUser(request);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();

        assertEquals("test@email.com", saved.getEmail());

        // check DOB conversion (Instant not null)
        assertNotNull(saved.getDob());
        assertTrue(saved.getDob() instanceof Instant);

        // response dob format MMddyy
        assertEquals("123199", response.getDob());
    }

    @Test
    void createUser_shouldFormatPhoneCorrectly() {

        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Phone Test");
        request.setDob("01-01-2000");
        request.setEmail("a@gmail.com");
        request.setPhone("0123456789");
        request.setOffice("SG");
        request.setRole(Role.MEMBER);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResponse response = userService.createUser(request);

        // then
        assertEquals("0123 456 789", response.getPhone());
    }

    @Test
    void createUser_shouldNotFail_whenOptionalFieldsNull() {

        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Minimal");
        request.setDob("01-01-2000");
        request.setEmail("min@gmail.com");
        request.setPhone("1234567890");
        request.setOffice("HN");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResponse response = userService.createUser(request);

        // then
        assertNotNull(response);
        assertEquals("OTHER", response.getRole());
        assertNotNull(response.getDob());
    }
}
