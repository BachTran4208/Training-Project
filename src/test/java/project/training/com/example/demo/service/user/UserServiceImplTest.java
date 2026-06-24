package project.training.com.example.demo.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UpdateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.entity.User;
import project.training.com.example.demo.exception.AppException;
import project.training.com.example.demo.mapper.UserMapper;
import project.training.com.example.demo.repository.UserRepository;
import project.training.com.example.demo.security.JwtService;
import project.training.com.example.demo.service.user.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    private UserMapper userMapper = new UserMapper();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserServiceImpl userService;

    

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper, jwtService, passwordEncoder);
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

    @Test
    void updateUser_shouldSuccess_whenOwnerUpdatesSelf() {

        // given
        Long userId = 1L;
        String email = "user@gmail.com";

        User target = new User();
        target.setId(userId);
        target.setEmail(email);
        target.setRole(Role.MEMBER);

        User current = new User();
        current.setEmail(email);
        current.setRole(Role.MEMBER);

        UpdateUserRequest request = new UpdateUserRequest();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(target));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(current));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserResponse response = userService.updateUser(userId, email, request);

        // then
        assertNotNull(response);
        verify(userRepository).save(target);
    }

    @Test
    void updateUser_shouldThrow_whenTargetUserNotFound() {

        Long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest();

        assertThrows(AppException.class,
                () -> userService.updateUser(userId, "email@gmail.com", request));
    }

    @Test
    void updateUser_shouldThrow_whenCurrentUserNotFound() {

        Long userId = 1L;

        User target = new User();
        target.setId(userId);
        target.setEmail("target@gmail.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(target));

        when(userRepository.findByEmail("current@gmail.com"))
                .thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest();

        assertThrows(AppException.class,
                () -> userService.updateUser(userId, "current@gmail.com", request));
    }

    @Test
    void updateUser_shouldThrowUnauthorized_whenNotOwnerAndNotPrivileged() {

        Long userId = 1L;

        User target = new User();
        target.setId(userId);
        target.setEmail("target@gmail.com");
        target.setRole(Role.MEMBER);

        User current = new User();
        current.setEmail("other@gmail.com");
        current.setRole(Role.MEMBER);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(target));

        when(userRepository.findByEmail("other@gmail.com"))
                .thenReturn(Optional.of(current));

        UpdateUserRequest request = new UpdateUserRequest();

        assertThrows(AppException.class,
                () -> userService.updateUser(userId, "other@gmail.com", request));
    }

    @Test
    void updateUser_shouldThrow_whenOwnerTriesChangeRestrictedRole() {

        Long userId = 1L;
        String email = "user@gmail.com";

        User target = new User();
        target.setId(userId);
        target.setEmail(email);
        target.setRole(Role.MEMBER);

        User current = new User();
        current.setEmail(email);
        current.setRole(Role.MEMBER);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setRole(Role.PROJECT_OWNER); // attempt change

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(target));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(current));

        assertThrows(AppException.class,
                () -> userService.updateUser(userId, email, request));
    }

    @Test
    void updateUser_shouldAllow_whenScrumMasterUpdatesOtherUser() {

        Long userId = 1L;

        User target = new User();
        target.setId(userId);
        target.setEmail("target@gmail.com");
        target.setRole(Role.MEMBER);

        User current = new User();
        current.setEmail("scrum@gmail.com");
        current.setRole(Role.SCRUM_MASTER);

        UpdateUserRequest request = new UpdateUserRequest();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(target));

        when(userRepository.findByEmail("scrum@gmail.com"))
                .thenReturn(Optional.of(current));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(userId, "scrum@gmail.com", request);

        assertNotNull(response);
        verify(userRepository).save(target);
    }
}
