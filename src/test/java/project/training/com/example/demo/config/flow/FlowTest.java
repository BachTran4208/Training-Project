package project.training.com.example.demo.config.flow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.training.com.example.demo.config.IntegrationConfig;
import project.training.com.example.demo.config.router.TaskRouter;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        IntegrationConfig.class,
        Flow.class,
        TaskRouter.class
})
class FlowTest {

    @Autowired
    @Qualifier("inputChannel")
    private MessageChannel inputChannel;

    @Autowired
    @Qualifier("CREATE_TASK_CHANNEL")
    private DirectChannel createTaskChannel;

    @Autowired
    @Qualifier("errorChannel")
    private DirectChannel errorChannel;

    @Test
    void shouldRouteToCreateTaskChannel() {

        AtomicReference<Message<?>> received = new AtomicReference<>();

        createTaskChannel.subscribe(received::set);

        inputChannel.send(
                MessageBuilder.withPayload("task")
                        .setHeader("action", "CREATE_TASK")
                        .build()
        );

        assertNotNull(received.get());
        assertEquals("task", received.get().getPayload());
    }

    @Test
    void shouldRouteToErrorChannel() {

        AtomicReference<Message<?>> received = new AtomicReference<>();

        errorChannel.subscribe(received::set);

        inputChannel.send(
                MessageBuilder.withPayload("task")
                        .setHeader("action", "UNKNOWN")
                        .build()
        );

        assertNotNull(received.get());
        assertEquals("task", received.get().getPayload());
    }
}
