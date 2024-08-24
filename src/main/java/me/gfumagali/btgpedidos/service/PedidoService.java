package me.gfumagali.btgpedidos.service;

import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.listener.dto.OrderDto;
import me.gfumagali.btgpedidos.repository.ClientOrderRepository;
import me.gfumagali.btgpedidos.repository.OrderTotalValueRepository;
import me.gfumagali.btgpedidos.repository.model.ClientOrders;
import me.gfumagali.btgpedidos.repository.model.Order;
import me.gfumagali.btgpedidos.repository.model.OrderTotalValue;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final ClientOrderRepository clientOrderRepository;
    private final OrderTotalValueRepository orderTotalValueRepository;

    public void saveOrder(OrderDto pedidoDTO) {
        Order order = new Order(
                String.valueOf(pedidoDTO.getCodigoPedido()),
                LocalDateTime.now(),
                pedidoDTO.getItems()
        );

        ClientOrders clientOrders = clientOrderRepository.findById(pedidoDTO.getCodigoCliente()).orElseGet(() ->
                new ClientOrders(
                        pedidoDTO.getCodigoCliente(),
                        0,
                        new HashMap<>()
                ));

        clientOrders.getPedidos().put(pedidoDTO.getCodigoPedido(), order);
        clientOrders.setQuantidadePedidos(clientOrders.getQuantidadePedidos() + 1);

        clientOrderRepository.save(clientOrders);

        orderTotalValueRepository.save(new OrderTotalValue(
                pedidoDTO.getCodigoPedido(),
                pedidoDTO.getItems().stream().mapToDouble(item -> item.getPreco() * item.getQuantidade()).sum()
        ));
    }

}
