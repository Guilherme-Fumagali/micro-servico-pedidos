package me.gfumagali.btgpedidos.service;

import me.gfumagali.btgpedidos.model.documents.ClientOrders;
import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.model.documents.OrderTotalValue;
import me.gfumagali.btgpedidos.model.dto.ItemDTO;
import me.gfumagali.btgpedidos.model.dto.OrderDTO;
import me.gfumagali.btgpedidos.model.exception.ResourceNotFoundException;
import me.gfumagali.btgpedidos.model.mappers.OrderMapper;
import me.gfumagali.btgpedidos.model.mappers.OrderMapperImpl;
import me.gfumagali.btgpedidos.model.mappers.OrderTotalValueMapper;
import me.gfumagali.btgpedidos.model.mappers.OrderTotalValueMapperImpl;
import me.gfumagali.btgpedidos.repository.ClientOrderRepository;
import me.gfumagali.btgpedidos.repository.OrderTotalValueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderTotalValueRepository orderTotalValueRepository;

    @Mock
    private ClientOrderRepository clientOrderRepository;

    @Spy
    private OrderTotalValueMapper orderTotalValueMapper = new OrderTotalValueMapperImpl();

    @Spy
    private OrderMapper orderMapper = new OrderMapperImpl();

    @InjectMocks
    private OrderService orderService;

    @Test
    public void whenGetTotalValue_thenReturnTotalValue() {
        // Given
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.of(new OrderTotalValue(1L, 100.0)));

        // When
        String totalValue = orderService.getTotalValue(1L);

        // Then
        verify(orderTotalValueRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(orderTotalValueRepository);
        assertEquals("100.0", totalValue);
    }

    @Test
    public void whenGetTotalValue_thenThrowResourceNotFoundException() {
        // Given
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getTotalValue(1L));

        // Then
        verify(orderTotalValueRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(orderTotalValueRepository);
        assertEquals("Pedido não encontrado", exception.getMessage());
    }

    @Test
    public void whenGetTotalValueWithFloat_thenReturnTotalValue() {
        // Given
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.of(new OrderTotalValue(1L, 100.31)));

        // When
        String totalValue = orderService.getTotalValue(1L);

        // Then
        verify(orderTotalValueRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(orderTotalValueRepository);
        assertEquals("100.31", totalValue);
    }

    @Test
    public void whenGetTotalValueWithZero_thenReturnTotalValue() {
        // Given
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.of(new OrderTotalValue(1L, 0.0)));

        // When
        String totalValue = orderService.getTotalValue(1L);

        // Then
        verify(orderTotalValueRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(orderTotalValueRepository);
        assertEquals("0.0", totalValue);
    }

    @Test
    public void whenGetOrdersQuantity_thenReturnOrdersQuantity() {
        // Given
        ClientOrders ClientOrders = new ClientOrders(1L, 3, new HashMap<>());
        when(clientOrderRepository.getOrdersQuantityByClientCode(1L)).thenReturn(Optional.of(ClientOrders));

        // When
        String ordersQuantity = orderService.getOrdersQuantity(1L);

        // Then
        verify(clientOrderRepository, times(1)).getOrdersQuantityByClientCode(1L);
        verifyNoMoreInteractions(clientOrderRepository);
        assertEquals("3", ordersQuantity);
    }

    @Test
    public void whenGetOrdersQuantity_thenThrowResourceNotFoundException() {
        // Given
        when(clientOrderRepository.getOrdersQuantityByClientCode(1L)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getOrdersQuantity(1L));

        // Then
        verify(clientOrderRepository, times(1)).getOrdersQuantityByClientCode(1L);
        verifyNoMoreInteractions(clientOrderRepository);
        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    public void whenGetOrders_thenReturnOrders() {
        // Given
        Order order1 = new Order(1L, LocalDateTime.now(), List.of(getExampleItemDTO(1)));
        Order order2 = new Order(2L, LocalDateTime.now(), List.of(getExampleItemDTO(2)));
        HashMap<Long, Order> orders = new HashMap<>();
        orders.put(1L, order1);
        orders.put(2L, order2);
        ClientOrders clientOrders = new ClientOrders(1L, 2, orders);
        when(clientOrderRepository.getOrdersByClientCode(1L)).thenReturn(Optional.of(clientOrders));

        // When
        List<Order> ordersList = orderService.getOrders(1L);

        // Then
        verify(clientOrderRepository, times(1)).getOrdersByClientCode(1L);
        verifyNoMoreInteractions(clientOrderRepository);
        assertEquals(2, ordersList.size());
        assertEquals(1L, ordersList.get(0).getOrderCode());
        assertEquals(2L, ordersList.get(1).getOrderCode());
    }


    @Test
    public void whenCreateOrder_thenStoreOrder() {
        // Given
        List<ItemDTO> items = List.of(
                getExampleItemDTO(1, 25.5),
                getExampleItemDTO(1, 25.75),
                getExampleItemDTO(3, 16.25)
        );
        OrderDTO orderDTO = new OrderDTO(1L, 1L, items);

        ArgumentCaptor<ClientOrders> clientOrdersCaptor = ArgumentCaptor.forClass(ClientOrders.class);
        ArgumentCaptor<OrderTotalValue> orderTotalValueCaptor = ArgumentCaptor.forClass(OrderTotalValue.class);

        when(clientOrderRepository.findById(1L)).thenReturn(Optional.empty()); // First order for client

        // When
        orderService.create(orderDTO);

        // Then
        verify(clientOrderRepository, times(1)).save(clientOrdersCaptor.capture());
        verify(orderTotalValueRepository, times(1)).save(orderTotalValueCaptor.capture());
        verifyNoMoreInteractions(clientOrderRepository, orderTotalValueRepository);

        assertEquals(1L, clientOrdersCaptor.getValue().getClientCode());
        assertEquals(1, clientOrdersCaptor.getValue().getOrdersQuantity());
        assertEquals(1L, clientOrdersCaptor.getValue().getOrders().get(1L).getOrderCode());
        assertEquals(1, clientOrdersCaptor.getValue().getOrders().size());

        assertEquals(1L, orderTotalValueCaptor.getValue().getOrderCode());
        assertEquals(100.0, orderTotalValueCaptor.getValue().getTotalValue());
    }

    @Test
    public void whenCreateOrderWithExistingClient_thenStoreOrder() {
        // Given
        List<ItemDTO> items = List.of(
                getExampleItemDTO(1, 25.5),
                getExampleItemDTO(1, 25.75),
                getExampleItemDTO(3, 16.25)
        );
        OrderDTO orderDTO = new OrderDTO(2L, 1L, items);

        ArgumentCaptor<ClientOrders> clientOrdersCaptor = ArgumentCaptor.forClass(ClientOrders.class);
        ArgumentCaptor<OrderTotalValue> orderTotalValueCaptor = ArgumentCaptor.forClass(OrderTotalValue.class);

        Order order = new Order(1L, LocalDateTime.now(), items);
        HashMap<Long, Order> orders = new HashMap<>();
        orders.put(1L, order);
        ClientOrders clientOrders = new ClientOrders(1L, 1, orders);

        when(clientOrderRepository.findById(1L)).thenReturn(Optional.of(clientOrders));

        // When
        orderService.create(orderDTO);

        // Then
        verify(clientOrderRepository, times(1)).save(clientOrdersCaptor.capture());
        verify(orderTotalValueRepository, times(1)).save(orderTotalValueCaptor.capture());
        verifyNoMoreInteractions(clientOrderRepository, orderTotalValueRepository);

        assertEquals(1L, clientOrdersCaptor.getValue().getClientCode());
        assertEquals(2, clientOrdersCaptor.getValue().getOrdersQuantity());
        assertEquals(2, clientOrdersCaptor.getValue().getOrders().size());

        assertEquals(1L, clientOrdersCaptor.getValue().getOrders().get(1L).getOrderCode());
        assertEquals(2L, clientOrdersCaptor.getValue().getOrders().get(2L).getOrderCode());

        assertEquals(2L, orderTotalValueCaptor.getValue().getOrderCode());
        assertEquals(100.0, orderTotalValueCaptor.getValue().getTotalValue());
    }

    @Test
    public void whenCreateOrderWithSameId_thenOverwriteOrder() {
        // Given
        List<ItemDTO> newItems = List.of(
                getExampleItemDTO(1, 200.0)
        );
        OrderDTO orderDTO = new OrderDTO(1L, 1L, newItems);

        ArgumentCaptor<ClientOrders> clientOrdersCaptor = ArgumentCaptor.forClass(ClientOrders.class);
        ArgumentCaptor<OrderTotalValue> orderTotalValueCaptor = ArgumentCaptor.forClass(OrderTotalValue.class);

        List<ItemDTO> previouslyItems = List.of(
                getExampleItemDTO(1, 25.5),
                getExampleItemDTO(1, 25.75),
                getExampleItemDTO(3, 16.25)
        );
        Order savedOrder = new Order(1L, LocalDateTime.now(), previouslyItems);
        HashMap<Long, Order> orders = new HashMap<>();
        orders.put(1L, savedOrder);
        ClientOrders clientOrders = new ClientOrders(1L, 1, orders);

        when(clientOrderRepository.findById(1L)).thenReturn(Optional.of(clientOrders));

        // When
        orderService.create(orderDTO);

        // Then
        verify(clientOrderRepository, times(1)).save(clientOrdersCaptor.capture());
        verify(orderTotalValueRepository, times(1)).save(orderTotalValueCaptor.capture());
        verifyNoMoreInteractions(clientOrderRepository, orderTotalValueRepository);

        assertEquals(1L, clientOrdersCaptor.getValue().getClientCode());
        assertEquals(1, clientOrdersCaptor.getValue().getOrdersQuantity());
        assertEquals(1L, clientOrdersCaptor.getValue().getOrders().get(1L).getOrderCode());

        assertNotEquals(savedOrder, clientOrdersCaptor.getValue().getOrders().get(1L));
        assertEquals(1, clientOrdersCaptor.getValue().getOrders().get(1L).getItems().size());
        assertEquals(200.0, clientOrdersCaptor.getValue().getOrders().get(1L).getItems().getFirst().getPrice());

        assertEquals(1L, orderTotalValueCaptor.getValue().getOrderCode());
        assertEquals(200.0, orderTotalValueCaptor.getValue().getTotalValue());
    }

    @Test
    public void whenCreateOrderWithException_thenNotThrowException() {
        // Given
        List<ItemDTO> items = List.of(
                getExampleItemDTO(1, 25.5),
                getExampleItemDTO(1, 25.75),
                getExampleItemDTO(3, 16.25)
        );
        OrderDTO orderDTO = new OrderDTO(1L, 1L, items);

        when(clientOrderRepository.findById(1L)).thenThrow(new RuntimeException("Error"));

        // When & Then
        assertDoesNotThrow(() -> orderService.create(orderDTO));
    }


    private ItemDTO getExampleItemDTO(int quantity) {
        return new ItemDTO("Product", quantity, 10.0);
    }

    private ItemDTO getExampleItemDTO(int quantity, double price) {
        return new ItemDTO("Product", quantity, price);
    }
}
