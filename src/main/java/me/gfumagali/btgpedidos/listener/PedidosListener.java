package me.gfumagali.btgpedidos.listener;

import lombok.extern.slf4j.Slf4j;
import me.gfumagali.btgpedidos.model.dto.PedidoDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "pedidos")
public class PedidosListener {

    @RabbitHandler
    public void receive(@Payload PedidoDTO in) {
        log.info("Pedido recebido: {}", in);
    }
}
