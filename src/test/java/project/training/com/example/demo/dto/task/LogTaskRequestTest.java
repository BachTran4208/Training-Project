package project.training.com.example.demo.dto.task;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class LogTaskRequestTest {
    private Validator validator;

    private LogTaskRequest validRequest() {
        LogTaskRequest request = new LogTaskRequest();
        request.setDate(LocalDate.now());
        request.setSpentHour(1);
        return request;
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidation_WhenDateIsNull() {
        LogTaskRequest request = validRequest();
        request.setDate(null);

        Set<ConstraintViolation<LogTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("date"))
        );
    }

    @Test
    void shouldPassValidation_WhenDateIsValid() {
        LogTaskRequest request = validRequest();

        Set<ConstraintViolation<LogTaskRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "-1",
            "0"
    })
    void shouldFailValidation_SpentHourField(Integer spentHour) {
        LogTaskRequest request = validRequest();
        request.setSpentHour(spentHour);

        Set<ConstraintViolation<LogTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("spentHour"))
        );
    }

    @Test
    void shouldFailValidation_WhenSpentHourIsNull() {
        LogTaskRequest request = validRequest();
        request.setSpentHour(null);

        Set<ConstraintViolation<LogTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("spentHour"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "8",
            "24"
    })
    void shouldPassValidation_SpentHourField(Integer spentHour) {
        LogTaskRequest request = validRequest();
        request.setSpentHour(spentHour);

        Set<ConstraintViolation<LogTaskRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}
