package project.training.com.example.demo.config.router;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import project.training.com.example.demo.constants.action.TaskActions;
import project.training.com.example.demo.constants.channel.ChannelNames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class TaskRouterTest {

    private final TaskRouter taskRouter = new TaskRouter();

    static Stream<Arguments> provideActions() {
        return Stream.of(
                Arguments.of(TaskActions.CREATE_TASK, ChannelNames.CREATE_TASK_CHANNEL),
                Arguments.of("unknown", ChannelNames.ERROR_CHANNEL)
        );
    }

    @Test
    void shouldThrowIllegalArgumentException_whenActionIsNull() {
        Message<String> message = MessageBuilder
                .withPayload("anything")
                .build();

        assertThrows(IllegalArgumentException.class, () -> taskRouter.route(message));
    }

    @ParameterizedTest
    @MethodSource("provideActions")
    void shouldRouteToCorrectChannel_whenActionIsProvided(String action, String expectedChannel) {
        Message<String> message = MessageBuilder
                .withPayload("anything")
                .setHeader("action", action)
                .build();

        String result = taskRouter.route(message);

        assertEquals(expectedChannel, result);
    }
}
