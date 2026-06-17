package project.training.com.example.demo.config.router;

import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import project.training.com.example.demo.constants.MessageHeaders;
import project.training.com.example.demo.constants.action.TaskActions;
import project.training.com.example.demo.constants.channel.ChannelNames;

@Component
public class TaskRouter {

    @Router(inputChannel = ChannelNames.ROUTER_CHANNEL)
    public String route(Message<?> message) {

        String action = (String) message.getHeaders()
                .get(MessageHeaders.ACTION);

        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        return switch (action) {
            case TaskActions.CREATE_TASK ->
                    ChannelNames.CREATE_TASK_CHANNEL;
            case TaskActions.GET_TASK ->
                    ChannelNames.GET_TASK_CHANNEL;
            default ->
                    ChannelNames.ERROR_CHANNEL;
        };
    }
}
