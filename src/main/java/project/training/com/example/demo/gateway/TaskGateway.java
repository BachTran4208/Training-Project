package project.training.com.example.demo.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import project.training.com.example.demo.constants.Domain;
import project.training.com.example.demo.constants.MessageHeaders;
import project.training.com.example.demo.constants.action.TaskActions;
import project.training.com.example.demo.dto.task.CreateTaskRequest;
import project.training.com.example.demo.dto.task.LogTaskRequest;
import project.training.com.example.demo.dto.task.TaskResponse;
import project.training.com.example.demo.dto.task.UpdateTaskRequest;

@MessagingGateway
public interface TaskGateway {

    @Gateway(
            requestChannel = "inputChannel",
            headers = {
            @GatewayHeader(name = MessageHeaders.DOMAIN, value = Domain.TASK_DOMAIN),
            @GatewayHeader(name = MessageHeaders.ACTION, value = TaskActions.CREATE_TASK)
        }
    )
    TaskResponse createTask(CreateTaskRequest request);

	@Gateway(
			requestChannel = "inputChannel",
            headers = {
            @GatewayHeader(name = MessageHeaders.DOMAIN, value = Domain.TASK_DOMAIN),
            @GatewayHeader(name = MessageHeaders.ACTION, value = TaskActions.GET_TASK)
        }
	)
    TaskResponse getTask(Long taskId);

    @Gateway(
			requestChannel = "inputChannel",
            headers = {
            @GatewayHeader(name = MessageHeaders.DOMAIN, value = Domain.TASK_DOMAIN),
            @GatewayHeader(name = MessageHeaders.ACTION, value = TaskActions.UPDATE_TASK)
        }
	)
    TaskResponse updateTask(@Header("taskId") Long taskId, @Payload UpdateTaskRequest request);


    @Gateway(
			requestChannel = "inputChannel",
			headers = {
            @GatewayHeader(name = MessageHeaders.DOMAIN, value = Domain.TASK_DOMAIN),
            @GatewayHeader(name = MessageHeaders.ACTION, value = TaskActions.LOG_TASK)
        }
	)
    TaskResponse logTask(@Header("taskId") Long taskId, @Payload LogTaskRequest request);
}
