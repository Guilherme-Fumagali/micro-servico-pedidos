package me.gfumagali.btgpedidos.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListenerErrorHandler implements RabbitListenerErrorHandler {

    @Override
    /* Method deprecated and will be removed in the future at org.springframework.amqp package. */
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {
        logError(amqpMessage, message, exception);
        return null;
    }

    @Override
    public Object handleError(Message amqpMessage, Channel channel, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {
        logError(amqpMessage, message, exception);
        return RabbitListenerErrorHandler.super.handleError(amqpMessage, channel, message, exception);
    }

    private void logError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) {
        log.error("Error processing message {} with {} bytes", message, amqpMessage.getBody().length);

        if (exception.getCause() instanceof MethodArgumentNotValidException)
            log.error("Caused by validation error: {}", exception.getCause().getMessage());
        else
            log.error("Caused by: ", exception.getCause());
    }
}
