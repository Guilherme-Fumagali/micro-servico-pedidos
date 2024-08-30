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
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.of(new OrderTotalValue(1L, BigDecimal.valueOf(100.0))));

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
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.of(new OrderTotalValue(1L, BigDecimal.valueOf(100.31))));

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
        when(orderTotalValueRepository.findById(1L)).thenReturn(Optional.of(new OrderTotalValue(1L, BigDecimal.ZERO)));

        // When
        String totalValue = orderService.getTotalValue(1L);

        // Then
        verify(orderTotalValueRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(orderTotalValueRepository);
        assertEquals("0", totalValue);
    }

    @Test
    public void whenGetOrdersQuantity_thenReturnOrdersQuantity() {
        // Given
        ClientOrders ClientOrders = new ClientOrders(1L, 3L, new ArrayList<>());
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
        List<Order> orders = List.of(order1, order2);

        ClientOrders clientOrders = new ClientOrders(1L, 3L, orders);
        when(clientOrderRepository.getOrdersByClientCode(1L, 0, 10)).thenReturn(Optional.of(clientOrders));

        // When
        Page<Order> ordersPage = orderService.getOrders(1L, 0, 10);

        // Then
        verify(clientOrderRepository, times(1)).getOrdersByClientCode(1L, 0, 10);
        verifyNoMoreInteractions(clientOrderRepository);
        assertEquals(2, ordersPage.getTotalElements());
        assertEquals(1L, ordersPage.getContent().get(0).getOrderCode());
        assertEquals(2L, ordersPage.getContent().get(1).getOrderCode());

    }

    @Test
    public void whenGetOrdersWithMorePages_thenReturnOrders() {
        // Given
        Order order1 = new Order(1L, LocalDateTime.now(), List.of(getExampleItemDTO(1)));
        Order order2 = new Order(2L, LocalDateTime.now(), List.of(getExampleItemDTO(2)));
        List<Order> orders = List.of(order1, order2);

        ClientOrders clientOrders = new ClientOrders(1L, 3L, orders);
        when(clientOrderRepository.getOrdersByClientCode(1L, 0, 2)).thenReturn(Optional.of(clientOrders));

        // When
        Page<Order> ordersPage = orderService.getOrders(1L, 0, 2);

        // Then
        verify(clientOrderRepository, times(1)).getOrdersByClientCode(1L, 0, 2);
        verifyNoMoreInteractions(clientOrderRepository);
        assertEquals(3, ordersPage.getTotalElements());
        assertEquals(1L, ordersPage.getContent().get(0).getOrderCode());
        assertEquals(2L, ordersPage.getContent().get(1).getOrderCode());
    }

    @Test
    public void whenGetOrdersWithPageGreaterThanZero_thenCalculateOffset() {
        // Given
        Integer page = 2;
        Integer size = 10;
        ClientOrders clientOrders = new ClientOrders(1L, 3L, new ArrayList<>());
        when(clientOrderRepository.getOrdersByClientCode(1L, 20, 10)).thenReturn(Optional.of(clientOrders));

        // When
        orderService.getOrders(1L, page, size);

        // Then
        verify(clientOrderRepository, times(1)).getOrdersByClientCode(1L, 20, 10);
        verifyNoMoreInteractions(clientOrderRepository);
    }

    @Test
    public void whenCreateOrder_thenStoreOrder() {
        // Given
        List<ItemDTO> items = List.of(
                getExampleItemDTO(1, BigDecimal.valueOf(25.5)),
                getExampleItemDTO(1, BigDecimal.valueOf(25.75)),
                getExampleItemDTO(3, BigDecimal.valueOf(16.25))
        );
        OrderDTO orderDTO = new OrderDTO(1L, 1L, items);

        ArgumentCaptor<ClientOrders> clientOrdersCaptor = ArgumentCaptor.forClass(ClientOrders.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        ArgumentCaptor<OrderTotalValue> orderTotalValueCaptor = ArgumentCaptor.forClass(OrderTotalValue.class);

        // When
        orderService.create(orderDTO);

        // Then
        verify(clientOrderRepository, times(1)).save(clientOrdersCaptor.capture());
        verify(clientOrderRepository, times(1)).createOrder(eq(1L), orderCaptor.capture());
        verify(orderTotalValueRepository, times(1)).save(orderTotalValueCaptor.capture());

        ClientOrders clientOrders = clientOrdersCaptor.getValue();
        Order order = orderCaptor.getValue();

        assertEquals(1L, clientOrders.getClientCode());
        assertEquals(1L, order.getOrderCode());
        assertEquals(3, order.getItems().size());

        OrderTotalValue orderTotalValue = orderTotalValueCaptor.getValue();

        assertEquals(1L, orderTotalValue.getOrderCode());
    }

    @Test
    public void whenCreateOrderWithExistingClient_thenStoreOrder() {
        // Given
        List<ItemDTO> items = List.of(
                getExampleItemDTO(1, BigDecimal.valueOf(25.5)),
                getExampleItemDTO(1, BigDecimal.valueOf(25.75)),
                getExampleItemDTO(3, BigDecimal.valueOf(16.25))
        );
        OrderDTO orderDTO = new OrderDTO(2L, 1L, items);

        ArgumentCaptor<ClientOrders> clientOrdersCaptor = ArgumentCaptor.forClass(ClientOrders.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        ArgumentCaptor<OrderTotalValue> orderTotalValueCaptor = ArgumentCaptor.forClass(OrderTotalValue.class);

        // When
        orderService.create(orderDTO);

        // Then
        verify(clientOrderRepository, times(1)).save(clientOrdersCaptor.capture());
        verify(clientOrderRepository, times(1)).createOrder(eq(1L), orderCaptor.capture());
        verify(orderTotalValueRepository, times(1)).save(orderTotalValueCaptor.capture());

        ClientOrders clientOrdersSaved = clientOrdersCaptor.getValue();
        Order orderSaved = orderCaptor.getValue();

        assertEquals(1L, clientOrdersSaved.getClientCode());
        assertEquals(2, orderSaved.getOrderCode());

        assertEquals(2L, orderTotalValueCaptor.getValue().getOrderCode());
    }


    @Test
    public void whenCreateOrderWithException_throwException() {
        // Given
        List<ItemDTO> items = List.of(
                getExampleItemDTO(1, BigDecimal.valueOf(25.5)),
                getExampleItemDTO(1, BigDecimal.valueOf(25.75)),
                getExampleItemDTO(3, BigDecimal.valueOf(16.25))
        );
        OrderDTO orderDTO = new OrderDTO(1L, 1L, items);

        when(clientOrderRepository.save(any())).thenThrow(new RuntimeException("Error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> orderService.create(orderDTO));
    }


    private ItemDTO getExampleItemDTO(int quantity) {
        return new ItemDTO("Product", quantity, BigDecimal.valueOf(10));
    }

    private ItemDTO getExampleItemDTO(int quantity, BigDecimal price) {
        return new ItemDTO("Product", quantity, price);
    }
}
