package project.training.com.example.demo.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

import project.training.com.example.demo.constants.Domain;
import project.training.com.example.demo.constants.MessageHeaders;
import project.training.com.example.demo.constants.action.UserActions;
import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;

@MessagingGateway
public interface UserGateway {
    @Gateway(
        requestChannel = "inputChannel",
        headers = {
            @GatewayHeader(name = MessageHeaders.DOMAIN, value = Domain.USER_DOMAIN),
            @GatewayHeader(name = MessageHeaders.ACTION, value = UserActions.CREATE_USER)
        }
    )
    UserResponse createUser(CreateUserRequest request);
}
