package project.training.com.example.demo.dto.task;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

	static class TestCase {
		String name;
		CreateTaskRequest request;
		String expectedMessage;

		TestCase(String name, CreateTaskRequest request, String expectedMessage) {
			this.name = name;
			this.request = request;
			this.expectedMessage = expectedMessage;
		}
	}

	static Stream<TestCase> invalidCases() {
		return Stream.of(

			// ===== TITLE =====
            new TestCase(
                    "blank title",
                    new CreateTaskRequest() {{
                        setTitle("");
                        setPoint(5);
                        setEstimateTime(10);
                        setAssignee("John");
                    }},
                    "Title is required"
            ),

            new TestCase(
                    "title exceeds 100 characters",
                    new CreateTaskRequest() {{
                        setTitle("a".repeat(101));
                        setPoint(5);
                        setEstimateTime(10);
                        setAssignee("John");
                    }},
                    "Title must be <= 100 characters"
            ),

            // ===== POINT =====
            new TestCase(
                    "point null",
                    new CreateTaskRequest() {{
                        setTitle("Task");
                        setEstimateTime(10);
                        setAssignee("John");
                    }},
                    "Point must be one of: 1, 2, 3, 5, 8"
            ),


            new TestCase(
                    "point not in allowed set",
                    new CreateTaskRequest() {{
                        setTitle("Task");
                        setPoint(9);
                        setEstimateTime(10);
                        setAssignee("John");
                    }},
                    "Point must be one of: 1, 2, 3, 5, 8"
            ),

            // ===== ESTIMATE TIME =====
            new TestCase(
                    "negative estimate time",
                    new CreateTaskRequest() {{
                        setTitle("Task");
                        setPoint(5);
                        setEstimateTime(-1);
                        setAssignee("John");
                    }},
                    "Estimate time must be >= 0"
            ),

            // ===== ASSIGNEE =====
            new TestCase(
                    "assignee too long",
                    new CreateTaskRequest() {{
                        setTitle("Task");
                        setPoint(5);
                        setEstimateTime(10);
                        setAssignee("a".repeat(51));
                    }},
                    "Assignee name too long"
            )
		);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("invalidCases")
	void shouldFailValidation(TestCase tc) {
		Set<ConstraintViolation<CreateTaskRequest>> violations =
				validator.validate(tc.request);

		assertTrue(
				violations.stream()
						.anyMatch(v -> v.getMessage().equals(tc.expectedMessage))
		);
	}

}
