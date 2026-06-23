package project.training.com.example.demo.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;

@MessagingGateway
public interface TaskGateway {

    @Gateway(
            requestChannel = "inputChannel",
            headers = @GatewayHeader(
                    name = "action",
                    value = "CREATE_TASK"
            )
    )
    TaskResponse createTask(CreateTaskRequest request);

	@Gateway(
			requestChannel = "inputChannel",
			headers = @GatewayHeader(
					name = "action",
					value = "GET_TASK"
			)
	)
    TaskResponse getTask(Long taskId);

    @Gateway(
			requestChannel = "inputChannel",
			headers = @GatewayHeader(
					name = "action",
					value = "UPDATE_TASK"

			)
	)
    TaskResponse updateTask(@Header("taskId") Long taskId, @Payload UpdateTaskRequest request);


    @Gateway(
			requestChannel = "inputChannel",
			headers = @GatewayHeader(
					name = "action",
					value = "LOG_TASK"
			)
	)
    TaskResponse logTask(@Header("taskId") Long taskId, @Payload LogTaskRequest request);
}
