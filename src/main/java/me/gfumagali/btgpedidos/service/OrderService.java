package me.gfumagali.btgpedidos.service;

import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.listener.dto.OrderDTO;
import me.gfumagali.btgpedidos.repository.ClientOrderRepository;
import me.gfumagali.btgpedidos.repository.OrderTotalValueRepository;
import me.gfumagali.btgpedidos.repository.model.documents.ClientOrders;
import me.gfumagali.btgpedidos.repository.model.documents.Order;
import me.gfumagali.btgpedidos.repository.model.documents.OrderTotalValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ClientOrderRepository clientOrderRepository;
    private final OrderTotalValueRepository orderTotalValueRepository;

    @Transactional
    public void create(OrderDTO orderDTO) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> storeClientOrder(orderDTO));
            executor.submit(() -> storeOrderTotalValue(orderDTO));
            executor.shutdown();
        }
    }

    public String getTotalValue(Long id) {
        return orderTotalValueRepository.findById(id)
                .map(OrderTotalValue::getTotalValue)
                .map(String::valueOf)
                .orElse("Pedido não encontrado");
    }


    public String getOrdersQuantity(Long id) {
        return clientOrderRepository.getOrdersQuantityByClientCode(id)
                .map(ClientOrders::getOrdersQuantity)
                .map(String::valueOf)
                .orElse("Cliente não encontrado");
    }

    public List<Order> getOrders(Long id) {
        return clientOrderRepository.getOrdersByClientCode(id)
                .map(ClientOrders::getOrders)
                .map(HashMap::values)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    private void storeClientOrder(OrderDTO orderDTO) {
        Order order = new Order(
                orderDTO.getOrderCode(),
                LocalDateTime.now(),
                orderDTO.getItems()
        );

        ClientOrders clientOrders = clientOrderRepository.findById(orderDTO.getClientCode()).orElseGet(() ->
                new ClientOrders(
                        orderDTO.getClientCode(),
                        0L,
                        new HashMap<>()
                ));

        clientOrders.getOrders().put(orderDTO.getOrderCode(), order);
        clientOrders.setOrdersQuantity(clientOrders.getOrdersQuantity() + 1);

        clientOrderRepository.save(clientOrders);
    }

    private void storeOrderTotalValue(OrderDTO orderDTO) {
        OrderTotalValue orderTotalValue = new OrderTotalValue(
                orderDTO.getOrderCode(),
                orderDTO.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum()
        );
        orderTotalValueRepository.save(orderTotalValue);
    }

}
