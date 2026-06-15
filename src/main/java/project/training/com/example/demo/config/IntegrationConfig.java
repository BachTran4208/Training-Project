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
    public MessageChannel createUserChannel() {
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
