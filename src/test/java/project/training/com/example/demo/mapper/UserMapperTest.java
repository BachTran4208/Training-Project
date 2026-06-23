package project.training.com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.entity.User;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toUserResponse_shouldReturnNull_whenUserIsNull() {

        UserResponse response = userMapper.toUserResponse(null);

        assertNull(response);
    }

    @Test
    void toUserResponse_shouldMapCorrectly() {

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setDob(Instant.parse("2000-01-15T00:00:00Z"));
        user.setEmail("john@example.com");
        user.setPhone("0123456789");
        user.setOffice("HCM");
        user.setRole(Role.PROJECT_OWNER);

        UserResponse response = userMapper.toUserResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("011500", response.getDob());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("0123 456 789", response.getPhone());
        assertEquals("HCM", response.getOffice());
        assertEquals("PROJECT_OWNER", response.getRole());
    }

    @Test
    void toUserResponse_shouldReturnNullDob_whenDobIsNull() {

        User user = new User();
        user.setRole(Role.MEMBER);

        UserResponse response = userMapper.toUserResponse(user);

        assertNull(response.getDob());
    }

    @ParameterizedTest
    @CsvSource({
            "0123456789, '0123 456 789'",
            "12345, 12345",
            "12345678901, 12345678901"
    })
    void toUserResponse_shouldFormatPhoneCorrectly(
            String phone,
            String expected) {

        User user = new User();
        user.setPhone(phone);
        user.setRole(Role.MEMBER);

        UserResponse response = userMapper.toUserResponse(user);

        assertEquals(expected, response.getPhone());
    }

    @Test
    void toUser_shouldReturnNull_whenRequestIsNull() {

        User user = userMapper.toUser(null);

        assertNull(user);
    }

    @Test
    void toUser_shouldMapCorrectly() {

        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setDob("01-15-2000");
        request.setEmail("JOHN@EXAMPLE.COM");
        request.setPhone("0123456789");
        request.setOffice("HCM");
        request.setRole(Role.SCRUM_MASTER);

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(
                Instant.parse("2000-01-15T00:00:00Z"),
                user.getDob()
        );
        assertEquals("john@example.com", user.getEmail());
        assertEquals("0123456789", user.getPhone());
        assertEquals("HCM", user.getOffice());
        assertEquals(Role.SCRUM_MASTER, user.getRole());
    }

    @ParameterizedTest
    @CsvSource({
            "01-15-2000, 2000-01-15T00:00:00Z",
            "12-25-2020, 2020-12-25T00:00:00Z",
            "02-29-2024, 2024-02-29T00:00:00Z"
    })
    void convertDob_shouldConvertCorrectly(
            String input,
            String expected) {

        Instant result = userMapper.convertDob(input);

        assertEquals(
                Instant.parse(expected),
                result
        );
    }

    @ParameterizedTest
    @CsvSource({
            "2000-01-15",
            "15-01-2000",
            "01/15/2000",
            "invalid"
    })
    void convertDob_shouldThrowException_whenFormatInvalid(
            String input) {

        assertThrows(
                Exception.class,
                () -> userMapper.convertDob(input)
        );
    }
}
