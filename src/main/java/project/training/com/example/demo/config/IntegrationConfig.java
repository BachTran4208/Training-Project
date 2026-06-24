package project.training.com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean("inputChannel")
    public MessageChannel inputChannel() { return new DirectChannel(); }

    @Bean("outputChannel")
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean("CREATE_TASK_CHANNEL")
    public MessageChannel createTaskChannel() {
        return new DirectChannel();
    }

    @Bean("GET_TASK_CHANNEL")
    public MessageChannel getTaskChannel() {
        return new DirectChannel();
    }

    @Bean("UPDATE_TASK_CHANNEL")
    public MessageChannel updateTaskChannel() {
        return new DirectChannel();
    }

    @Bean("LOG_TASK_CHANNEL")
    public MessageChannel logTaskChannel() {
        return new DirectChannel();
    }

    @Bean("CREATE_USER_CHANNEL")
    public MessageChannel createUserChannel() {
        return new DirectChannel();
    }

    @Bean("UPDATE_USER_CHANNEL")
    public MessageChannel updateUserChannel() {
        return new DirectChannel();
    }

    @Bean("LOGIN_USER_CHANNEL")
    public MessageChannel loginUserChannel() {
        return new DirectChannel();
    }

    @Bean("DOMAIN_CHANNEL")
    public MessageChannel domainChannel() {
        return new DirectChannel();
    }

    @Bean("errorChannel")
    public MessageChannel errorChannel() {
        return new DirectChannel();
    }

    @Bean("routerChannel")
    public MessageChannel routerChannel() {
        return new DirectChannel();
    }
}
