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

public class CreateUserRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private CreateUserRequest validRequest() {
        CreateUserRequest request = new CreateUserRequest();
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
            "'',Name is required",
            "'   ',Name is required"
    })
    void shouldFailValidation_NameField(
            String name,
            String expectedMessage
    ) {
        CreateUserRequest request = validRequest();
        request.setName(name);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @Test
    void shouldPassValidation_WhenNameIsValid() {
        CreateUserRequest request = validRequest();

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "abc",
            "@gmail.com",
            "abc.com"
    })
    void shouldFailValidation_EmailField(String email) {
        CreateUserRequest request = validRequest();
        request.setEmail(email);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath()
                                .toString()
                                .equals("email"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "john@gmail.com",
            "test123@yahoo.com",
            "user@company.vn"
    })
    void shouldPassValidation_EmailField(String email) {
        CreateUserRequest request = validRequest();
        request.setEmail(email);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "'',Office is not blank",
            "'   ',Office is not blank"
    })
    void shouldFailValidation_OfficeField(
            String office,
            String expectedMessage
    ) {
        CreateUserRequest request = validRequest();
        request.setOffice(office);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @Test
    void shouldPassValidation_OfficeField() {
        CreateUserRequest request = validRequest();
        request.setOffice("HCM");

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'',Dob is not blank",
            "'   ',Dob is not blank",
            "null,Dob is not blank",
            "2024-01-01,DOB must be in MM/dd/yyyy format",
            "13-01-2024,DOB must be in MM/dd/yyyy format"
    }, nullValues = "null")
    void shouldFailValidation_DobField(
            String dob,
            String expectedMessage
    ) {
        CreateUserRequest request = validRequest();
        request.setDob(dob);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "01-01-2000",
            "12-31-1999",
            "02-29-2024"
    })
    void shouldPassValidation_DobField(String dob) {
        CreateUserRequest request = validRequest();
        request.setDob(dob);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {
            ",Phone is not blank",
            "123,Phone must contain exactly 10 digits",
            "123456789,Phone must contain exactly 10 digits",
            "12345678901,Phone must contain exactly 10 digits",
            "abcdefghij,Phone must contain exactly 10 digits",
            "01234abcde,Phone must contain exactly 10 digits"
    }, nullValues = "null")
    void shouldFailValidation_PhoneField(
            String phone,
            String expectedMessage
    ) {
        CreateUserRequest request = validRequest();
        request.setPhone(phone);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0123456789",
            "0987654321",
            "1111111111"
    })
    void shouldPassValidation_PhoneField(String phone) {
        CreateUserRequest request = validRequest();
        request.setPhone(phone);

        Set<ConstraintViolation<CreateUserRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}
