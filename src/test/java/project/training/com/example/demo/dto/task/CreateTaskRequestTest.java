package project.training.com.example.demo.dto.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateTaskRequestTest {
    
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_WhenRequestIsValid() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Implement Login API");
        request.setAssignee("John");
        request.setPoint(5);
        request.setEstimateTime(10);

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFail_WhenTitleIsBlank() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");
        request.setPoint(5);

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Title is required",
                violations.iterator().next().getMessage());
    }

    @Test
    void shouldFail_WhenTitleExceeds100Characters() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("a".repeat(101));
        request.setPoint(5);

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Title must be <= 100 characters"))
        );
    }

    @Test
    void shouldFail_WhenPointIsNull() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Point is required"))
        );
    }

    @Test
    void shouldFail_WhenPointLessThan1() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setPoint(0);

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Point must be >= 1"))
        );
    }

    @Test
    void shouldFail_WhenPointGreaterThan8() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setPoint(9);

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Point must be <= 8"))
        );
    }

    @Test
    void shouldFail_WhenEstimateTimeIsNegative() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setPoint(5);
        request.setEstimateTime(-1);

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Estimate time must be >= 0"))
        );
    }

    @Test
    void shouldFail_WhenAssigneeTooLong() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setPoint(5);
        request.setAssignee("a".repeat(51));

        Set<ConstraintViolation<CreateTaskRequest>> violations =
                validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Assignee name too long"))
        );
    }
}
