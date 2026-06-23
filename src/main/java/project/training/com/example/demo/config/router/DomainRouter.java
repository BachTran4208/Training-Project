package project.training.com.example.demo.config.router;

import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import project.training.com.example.demo.constants.Domain;
import project.training.com.example.demo.constants.MessageHeaders;
import project.training.com.example.demo.constants.channel.ChannelNames;

@Component
public class DomainRouter {

    @Router(inputChannel = ChannelNames.DOMAIN_CHANNEL)
    public String route(Message<?> message) {

        String domain = (String) message.getHeaders()
                .get(MessageHeaders.DOMAIN);

        if (domain == null) {
            return ChannelNames.ERROR_CHANNEL;
        } 

        return switch (domain) {
            case Domain.USER_DOMAIN ->
                    ChannelNames.USER_ROUTER_CHANNEL;
            case Domain.TASK_DOMAIN ->
                    ChannelNames.TASK_ROUTER_CHANNEL;
            default ->
                    ChannelNames.ERROR_CHANNEL;
        };
    }
}
