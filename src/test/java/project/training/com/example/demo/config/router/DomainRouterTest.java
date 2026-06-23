package project.training.com.example.demo.config.router;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import project.training.com.example.demo.constants.Domain;
import project.training.com.example.demo.constants.MessageHeaders;
import project.training.com.example.demo.constants.channel.ChannelNames;

@ExtendWith(MockitoExtension.class)
class DomainRouterTest {

    private final DomainRouter domainRouter = new DomainRouter();

    static Stream<Arguments> provideDomains() {
        return Stream.of(
                Arguments.of(Domain.USER_DOMAIN, ChannelNames.USER_ROUTER_CHANNEL),
                Arguments.of(Domain.TASK_DOMAIN, ChannelNames.TASK_ROUTER_CHANNEL),
                Arguments.of("unknown", ChannelNames.ERROR_CHANNEL)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDomains")
    void shouldRouteToCorrectChannel_whenDomainIsProvided(String domain, String expectedChannel) {

        Message<String> message = MessageBuilder
                .withPayload("anything")
                .setHeader(MessageHeaders.DOMAIN, domain)
                .build();

        String result = domainRouter.route(message);

        assertEquals(expectedChannel, result);
    }
}
