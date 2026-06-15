package project.training.com.example.demo.config.router;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TaskRouterTest {

    private final TaskRouter taskRouter = new TaskRouter();

    @Test
    void shouldThrowAssertionError_whenActionIsNull() {
        Message<String> message = MessageBuilder
                .withPayload("anything")
                .build();

        assertThrows(AssertionError.class, () -> taskRouter.route(message));
    }

    @Test
    void shouldRouteToCreateTaskChannel_whenActionIsCreateTask() {
        Message<String> message = MessageBuilder
                .withPayload("anything")
                .setHeader("action", "CREATE_TASK")
                .build();

        String result = taskRouter.route(message);

        assertEquals("CREATE_TASK_CHANNEL", result);
    }

    @Test
    void shouldRouteToErrorChannel_whenActionIsUnknown() {
        Message<String> message = MessageBuilder
                .withPayload("anything")
                .setHeader("action", "UNKNOWN")
                .build();

        String result = taskRouter.route(message);

        assertEquals("errorChannel", result);
    }
}
