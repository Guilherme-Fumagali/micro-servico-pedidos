package me.gfumagali.btgpedidos.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gfumagali.btgpedidos.listener.dto.OrderDto;
import me.gfumagali.btgpedidos.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "pedidos")
public class PedidosListener {
    private final PedidoService pedidoService;

    @RabbitHandler
    public void receive(@Payload OrderDto in) {
        log.info("Pedido recebido: {}", in.getCodigoPedido());
        pedidoService.saveOrder(in);
    }
}
