package me.gfumagali.btgpedidos.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gfumagali.btgpedidos.model.dto.listener.OrderDTO;
import me.gfumagali.btgpedidos.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "${application.listener.queue.name}")
public class OrderListener {
    private final OrderService orderService;

    @RabbitHandler
    public void receive(@Payload OrderDTO in) {
        log.info("Order received: {} - {}", in.getOrderCode(), in.getClientCode());
        log.trace("Items: {}", in.getItems());
        orderService.create(in);
    }
}
