package project.training.com.example.demo.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;

@MessagingGateway
public interface TaskGateway {

    @Gateway(
            requestChannel = "inputChannel",
            replyChannel = "outputChannel",
            headers = @GatewayHeader(
                    name = "action",
                    value = "CREATE_TASK"
            )
    )
    TaskResponse createTask(CreateTaskRequest request);
}
