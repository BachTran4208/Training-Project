package project.training.com.example.demo.config.router;

import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class TaskRouter {

    @Router
    public String route(Message<?> message) {

        String action = (String) message.getHeaders().get("action");

        assert action != null;
        return switch (action) {
            case "CREATE_TASK" -> "CREATE_TASK_CHANNEL";
            default -> "errorChannel";
        };
    }
}
