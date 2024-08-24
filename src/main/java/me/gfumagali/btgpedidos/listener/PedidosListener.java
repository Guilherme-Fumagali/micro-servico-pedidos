package me.gfumagali.btgpedidos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@Slf4j
@RabbitListener(queues = "pedidos")
public class PedidosListener {

    @RabbitHandler(isDefault = true)
    public void receive(@Payload LinkedHashMap<String, Object> in) {
        log.info("Pedido recebido: {}", in);
    }
}
