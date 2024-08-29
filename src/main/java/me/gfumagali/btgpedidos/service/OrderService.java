package me.gfumagali.btgpedidos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gfumagali.btgpedidos.model.documents.ClientOrders;
import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.model.documents.OrderTotalValue;
import me.gfumagali.btgpedidos.model.dto.OrderDTO;
import me.gfumagali.btgpedidos.model.exception.ResourceNotFoundException;
import me.gfumagali.btgpedidos.model.mappers.OrderMapper;
import me.gfumagali.btgpedidos.model.mappers.OrderTotalValueMapper;
import me.gfumagali.btgpedidos.repository.ClientOrderRepository;
import me.gfumagali.btgpedidos.repository.OrderTotalValueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ClientOrderRepository clientOrderRepository;
    private final OrderTotalValueRepository orderTotalValueRepository;

    private final OrderTotalValueMapper orderTotalValueMapper;
    private final OrderMapper orderMapper;

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
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }


    public String getOrdersQuantity(Long id) {
        log.debug("Retrieving quantity of orders for client {}", id);
        return clientOrderRepository.getOrdersQuantityByClientCode(id)
                .map(ClientOrders::getOrdersQuantity)
                .map(String::valueOf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Page<Order> getOrders(Long id, Integer page, Integer size) {
        log.debug("Retrieving orders for client {} with page {} and size {}", id, page, size);
        final var skip = page * size;
        ClientOrders clientOrders = clientOrderRepository.getOrdersByClientCode(id, skip, size)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return new PageImpl<>(clientOrders.getOrders().values().stream().toList(), PageRequest.of(page, size), clientOrders.getOrdersQuantity());
    }

    private void storeClientOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toDocument(orderDTO);
        log.trace("Storing order {}", order);

        createClientIfNotExists(orderDTO);
        appendOrder(orderDTO.getClientCode(), order);

        log.debug("stored order {} for client {}", orderDTO.getOrderCode(), orderDTO.getClientCode());
    }

    private void storeOrderTotalValue(OrderDTO orderDTO) {
        OrderTotalValue orderTotalValue = orderTotalValueMapper.toDocument(orderDTO);
        log.debug("Storing total value {} for order {}", orderTotalValue.getTotalValue(), orderDTO.getOrderCode());
        orderTotalValueRepository.save(orderTotalValue);
    }

    private void createClientIfNotExists(OrderDTO orderDTO) {
        if (!clientOrderRepository.existsById(orderDTO.getClientCode())) {
            log.debug("Client {} not found, initializing new document", orderDTO.getClientCode());
            clientOrderRepository.save(new ClientOrders(
                    orderDTO.getClientCode(),
                    0,
                    new HashMap<>()
            ));
        }
    }

    private void appendOrder(Long clientCode, Order order) {
        clientOrderRepository.upsertOrderByClientCode(
                clientCode,
                order.getOrderCode(),
                order
        );
    }

}
