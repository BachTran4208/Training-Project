package project.training.com.example.demo.dto.task;

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

public class UpdateTaskRequestTest {

    private Validator validator;

    private UpdateTaskRequest validRequest() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setAssignee("John");
        request.setPoint(1);
        request.setEstimateTime(10);
        return request;
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'',assignee cannot be blank",
            "'   ',assignee cannot be blank"
    })
    void shouldFailValidation_AssigneeField(
            String assignee,
            String expectedMessage
    ) {
        UpdateTaskRequest request = validRequest();
        request.setAssignee(assignee);

        Set<ConstraintViolation<UpdateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @Test
    void shouldPassValidation_WhenAssigneeIsNull() {
        UpdateTaskRequest request = validRequest();

        request.setAssignee(null);
        request.setPoint(1);
        request.setEstimateTime(10);


        Set<ConstraintViolation<UpdateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "-7 = Point must be one of: 1, 2, 3, 5, 8",
            "4 = Point must be one of: 1, 2, 3, 5, 8",
            "6 = Point must be one of: 1, 2, 3, 5, 8",
            "10 = Point must be one of: 1, 2, 3, 5, 8",
            "null = Point must be one of: 1, 2, 3, 5, 8"
    }, delimiter = '=', nullValues = "null")
    void shouldFailValidation_PointField(
            Integer point,
            String expectedMessage
    ) {
        UpdateTaskRequest request = validRequest();
        request.setPoint(point);

        Set<ConstraintViolation<UpdateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "3",
            "5",
            "8"
    })
    void shouldPassValidation_PointField(Integer point) {
        UpdateTaskRequest request = validRequest();
        request.setPoint(point);

        Set<ConstraintViolation<UpdateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @ParameterizedTest
    @CsvSource(value = {
            "-1 , Estimate time must be >= 0",
            "-10 , Estimate time must be >= 0"
    }, delimiter = ',', nullValues = "null")
    void shouldFailValidation_EstimateTimeField(
            Integer estimateTime,
            String expectedMessage
    ) {
        UpdateTaskRequest request = validRequest();
        request.setEstimateTime(estimateTime);

        Set<ConstraintViolation<UpdateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage))
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0",
            "1",
            "100",
            "null"
    }, nullValues = "null")
    void shouldPassValidation_EstimateTimeField(Integer estimateTime) {
        UpdateTaskRequest request = validRequest();

        request.setEstimateTime(estimateTime);

        Set<ConstraintViolation<UpdateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
    
}
