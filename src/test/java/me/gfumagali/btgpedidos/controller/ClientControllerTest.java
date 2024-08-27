package me.gfumagali.btgpedidos.controller;

import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.model.dto.ItemDTO;
import me.gfumagali.btgpedidos.model.exception.ResourceNotFoundException;
import me.gfumagali.btgpedidos.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void whenGetOrdersQuantity_thenReturnOrdersQuantity() throws Exception {
        // Given
        when(orderService.getOrdersQuantity(1L)).thenReturn("10");

        // When & Then
        mockMvc.perform(get("/cliente/pedidos/1/quantidade"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(orderService, times(1)).getOrdersQuantity(1L);
    }

    @Test
    public void whenGetOrders_thenReturnOrders() throws Exception {
        // Given
        Order order = new Order(1L, LocalDateTime.now(), List.of(
                getExampleItemDTO(1, 25.5),
                getExampleItemDTO(1, 25.75),
                getExampleItemDTO(3, 16.25)
        ));

        when(orderService.getOrders(1L)).thenReturn(List.of(order));

        // When & Then
        mockMvc.perform(get("/cliente/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + getJson(order) + "]"));

        verify(orderService, times(1)).getOrders(1L);
    }

    @Test
    public void whenGetOrdersQuantity_thenThrowResourceNotFoundException() throws Exception {
        // Given
        when(orderService.getOrdersQuantity(1L)).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // When & Then
        mockMvc.perform(get("/cliente/pedidos/1/quantidade"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));

        verify(orderService, times(1)).getOrdersQuantity(1L);
    }

    @Test
    public void whenGetOrders_thenThrowResourceNotFoundException() throws Exception {
        // Given
        when(orderService.getOrders(1L)).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // When & Then
        mockMvc.perform(get("/cliente/pedidos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));

        verify(orderService, times(1)).getOrders(1L);
    }

    private String getJson(Order order) {
        return "{\"codigoPedido\":" + order.getOrderCode() + ",\"dataPedido\":\"" + order.getOrderDate() + "\",\"itens\":[" + order.getItems().stream().map(this::getJson).reduce((a, b) -> a + "," + b).orElse("") + "]}";
    }

    private String getJson(ItemDTO item) {
        return "{\"produto\":\"" + item.getProduct() + "\",\"quantidade\":" + item.getQuantity() + ",\"preco\":" + item.getPrice() + "}";
    }

    private ItemDTO getExampleItemDTO(int quantity) {
        return new ItemDTO("Product", quantity, 10.0);
    }

    private ItemDTO getExampleItemDTO(int quantity, double price) {
        return new ItemDTO("Product", quantity, price);
    }
}
