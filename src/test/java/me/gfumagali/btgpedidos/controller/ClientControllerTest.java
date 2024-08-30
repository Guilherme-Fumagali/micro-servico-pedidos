package me.gfumagali.btgpedidos.controller;

import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.model.dto.ItemDTO;
import me.gfumagali.btgpedidos.model.exception.ResourceNotFoundException;
import me.gfumagali.btgpedidos.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
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
        Order order = new Order(1L, LocalDateTime.of(2021, 1, 1, 0, 0), List.of(
                getExampleItemDTO(1, BigDecimal.valueOf(25.5)),
                getExampleItemDTO(1, BigDecimal.valueOf(25.75)),
                getExampleItemDTO(3, BigDecimal.valueOf(16.25))
        ));

        when(orderService.getOrders(1L, 0, 10)).thenReturn(new PageImpl<>(List.of(order)));

        // When & Then
        mockMvc.perform(get("/cliente/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(getJson(order))));

        verify(orderService, times(1)).getOrders(1L, 0, 10);
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
        when(orderService.getOrders(1L, 0, 10)).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        // When & Then
        mockMvc.perform(get("/cliente/pedidos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));

        verify(orderService, times(1)).getOrders(1L, 0, 10);
    }

    private String getJson(Order order) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").toFormatter();
        String isoDate = order.getOrderDate().format(formatter);
        return "{\"codigoPedido\":" + order.getOrderCode() + ",\"dataPedido\":\"" + isoDate + "\",\"itens\":[" + order.getItems().stream().map(this::getJson).reduce((a, b) -> a + "," + b).orElse("") + "]}";
    }

    private String getJson(ItemDTO item) {
        return "{\"produto\":\"" + item.getProduct() + "\",\"quantidade\":" + item.getQuantity() + ",\"preco\":" + item.getPrice() + "}";
    }
    

    private ItemDTO getExampleItemDTO(int quantity, BigDecimal price) {
        return new ItemDTO("Product", quantity, price);
    }
}
