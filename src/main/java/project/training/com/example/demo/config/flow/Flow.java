package project.training.com.example.demo.config.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import project.training.com.example.demo.constants.channel.ChannelNames;

@Configuration
public class Flow {

    @Bean
    public IntegrationFlow mainFlow() {
        return IntegrationFlow.from(ChannelNames.INPUT_CHANNEL)
                .log(message -> "INPUT: " + message.getPayload())
                .channel(ChannelNames.DOMAIN_CHANNEL)
                .get();
    }

    @Bean
    public IntegrationFlow errorFlow() {
        return IntegrationFlow.from(ChannelNames.ERROR_CHANNEL)
                .log(message -> "ERROR: " + message.getPayload())
                .get();
    }
}

