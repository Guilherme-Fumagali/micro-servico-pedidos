package me.gfumagali.btgpedidos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderService {
    private final ClientOrderRepository clientOrderRepository;
    private final OrderTotalValueRepository orderTotalValueRepository;

    @Transactional
    public void create(OrderDTO orderDTO) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> storeClientOrder(orderDTO));
            executor.submit(() -> storeOrderTotalValue(orderDTO));
            executor.shutdown();
            log.info("Completed consumption of order {}", orderDTO.getOrderCode());
        } catch (Exception e) {
            log.error("Error while consuming order {}", orderDTO.getOrderCode(), e);
        }
    }

    public String getTotalValue(Long id) {
        log.debug("Retrieving total value for order {}", id);
        return orderTotalValueRepository.findById(id)
                .map(OrderTotalValue::getTotalValue)
                .map(String::valueOf)
                .orElse("Pedido não encontrado");
    }


    public String getOrdersQuantity(Long id) {
        log.debug("Retrieving quantity of orders for client {}", id);
        return clientOrderRepository.getOrdersQuantityByClientCode(id)
                .map(ClientOrders::getOrdersQuantity)
                .map(String::valueOf)
                .orElse("Cliente não encontrado");
    }

    public List<Order> getOrders(Long id) {
        log.debug("Retrieving orders for client {}", id);
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
        log.trace("Storing order {}", order);

        ClientOrders clientOrders = clientOrderRepository.findById(orderDTO.getClientCode()).orElseGet(() -> {
            log.debug("Client {} not found, initializing new document", orderDTO.getClientCode());

            return new ClientOrders(
                    orderDTO.getClientCode(),
                    0L,
                    new HashMap<>()
            );
        });

        clientOrders.getOrders().put(orderDTO.getOrderCode(), order);
        clientOrders.setOrdersQuantity(clientOrders.getOrdersQuantity() + 1);
        log.trace("Quantity of orders for client {} is now {}", orderDTO.getClientCode(), clientOrders.getOrdersQuantity());

        log.debug("Storing order {} for client {}", orderDTO.getOrderCode(), orderDTO.getClientCode());
        clientOrderRepository.save(clientOrders);
    }

    private void storeOrderTotalValue(OrderDTO orderDTO) {
        OrderTotalValue orderTotalValue = new OrderTotalValue(
                orderDTO.getOrderCode(),
                orderDTO.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum()
        );

        log.debug("Storing total value {} for order {}", orderTotalValue.getTotalValue(), orderDTO.getOrderCode());
        orderTotalValueRepository.save(orderTotalValue);
    }

}
