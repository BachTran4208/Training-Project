package project.training.com.example.demo.config.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import project.training.com.example.demo.config.router.TaskRouter;
import project.training.com.example.demo.service.task.TaskService;

@Configuration
@RequiredArgsConstructor
public class TaskFlow {

    private final TaskRouter taskRouter;

    @Bean
    public IntegrationFlow createTask(TaskService taskService) {
        return IntegrationFlow
                .from("inputChannel")
                .log(message -> "INPUT: " + message.getPayload())
                .route(Message.class, taskRouter::route)
                .get();
    }
}
