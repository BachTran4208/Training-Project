package project.training.com.example.demo.dto.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import project.training.com.example.demo.entity.Role;

public class UpdateUserRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private UpdateUserRequest validRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("John Doe");
        request.setDob("01-01-2000");
        request.setEmail("john@gmail.com");
        request.setPhone("0912345678");
        request.setOffice("HCM");
        request.setRole(Role.MEMBER);

        return request;
    }

    @ParameterizedTest
    @CsvSource({
            "abc",
            "@gmail.com",
            "abc.com"
    })
    void shouldFailValidation_Email(String email) {
        UpdateUserRequest request = validRequest();
        request.setEmail(email);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("email"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "john@gmail.com",
            "test123@yahoo.com",
            "user@company.vn"
    })
    void shouldPassValidation_Email(String email) {
        UpdateUserRequest request = validRequest();
        request.setEmail(email);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-01",
            "13-01-2024",
            "' '"
    })
    void shouldFailValidation_Dob(String dob) {
        UpdateUserRequest request = validRequest();
        request.setDob(dob);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("dob"))
        );
    }

    @Test
    void shouldFailValidation_WhenDobIsNull() {
        UpdateUserRequest request = validRequest();
        request.setDob(null);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v ->
                                v.getPropertyPath().toString().equals("dob")
                                        && v.getMessage().equals("Dob is not blank"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "01-01-2000",
            "12-31-1999",
            "02-29-2024"
    })
    void shouldPassValidation_Dob(String dob) {
        UpdateUserRequest request = validRequest();
        request.setDob(dob);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "123",
            "123456789",
            "12345678901",
            "abcdefghij",
            "01234abcde"
    })
    void shouldFailValidation_Phone(String phone) {
        UpdateUserRequest request = validRequest();
        request.setPhone(phone);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("phone"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0123456789",
            "0987654321",
            "1111111111"
    })
    void shouldPassValidation_Phone(String phone) {
        UpdateUserRequest request = validRequest();
        request.setPhone(phone);

        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}
