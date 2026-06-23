package project.training.com.example.demo.config.router;

import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import project.training.com.example.demo.constants.MessageHeaders;
import project.training.com.example.demo.constants.action.UserActions;
import project.training.com.example.demo.constants.channel.ChannelNames;

@Component
public class UserRouter {
    
    @Router(inputChannel = ChannelNames.USER_ROUTER_CHANNEL)
    public String route(Message<?> message) {

        String action = (String) message.getHeaders()
                .get(MessageHeaders.ACTION);

        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        return switch (action) {
            case UserActions.CREATE_USER ->
                    ChannelNames.CREATE_USER_CHANNEL;
            default ->
                    ChannelNames.ERROR_CHANNEL;
        };
    }
}
