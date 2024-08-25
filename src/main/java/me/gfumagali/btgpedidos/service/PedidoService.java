package me.gfumagali.btgpedidos.service;

import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.listener.dto.OrderDto;
import me.gfumagali.btgpedidos.repository.ClientOrderRepository;
import me.gfumagali.btgpedidos.repository.OrderTotalValueRepository;
import me.gfumagali.btgpedidos.repository.model.ClientOrders;
import me.gfumagali.btgpedidos.repository.model.Order;
import me.gfumagali.btgpedidos.repository.model.OrderTotalValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final ClientOrderRepository clientOrderRepository;
    private final OrderTotalValueRepository orderTotalValueRepository;

    @Transactional
    public void saveOrder(OrderDto pedidoDTO) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> createClienteOrder(pedidoDTO));
            executor.submit(() -> createOrderTotalValue(pedidoDTO));
            executor.shutdown();
        }
    }

    public String getTotalValue(Long id) {
        return orderTotalValueRepository.findById(id)
                .map(OrderTotalValue::getValorTotal)
                .map(String::valueOf)
                .orElse("Pedido não encontrado");
    }


    public String getOrdersQuantity(Long id) {
        return clientOrderRepository.findById(id)
                .map(ClientOrders::getQuantidadePedidos)
                .map(String::valueOf)
                .orElse("Cliente não encontrado");
    }

    public List<Order> getOrders(Long id) {
        return clientOrderRepository.findById(id)
                .map(ClientOrders::getPedidos)
                .map(HashMap::values)
                .map(List::copyOf)
                .orElse(List.of());
    }

    private void createClienteOrder(OrderDto pedidoDTO) {
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
    }

    private void createOrderTotalValue(OrderDto pedidoDTO) {
        orderTotalValueRepository.save(new OrderTotalValue(
                pedidoDTO.getCodigoPedido(),
                pedidoDTO.getItems().stream().mapToDouble(item -> item.getPreco() * item.getQuantidade()).sum()
        ));
    }

}
