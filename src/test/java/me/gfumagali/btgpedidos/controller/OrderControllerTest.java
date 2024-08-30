package me.gfumagali.btgpedidos.controller;

import me.gfumagali.btgpedidos.model.exception.ResourceNotFoundException;
import me.gfumagali.btgpedidos.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void whenGetTotalValue_thenReturnTotalValue() throws Exception {
        // Given
        when(orderService.getTotalValue(1L)).thenReturn("100.0");

        // When & Then
        mockMvc.perform(get("/pedido/valor-total/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));

        verify(orderService, times(1)).getTotalValue(1L);
    }

    @Test
    public void whenGetTotalValue_thenThrowResourceNotFoundException() throws Exception {
        // Given
        when(orderService.getTotalValue(1L)).thenThrow(new ResourceNotFoundException("Pedido não encontrado"));

        // When & Then
        mockMvc.perform(get("/pedido/valor-total/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Pedido não encontrado"));

        verify(orderService, times(1)).getTotalValue(1L);
    }

    @Test
    public void whenGetTotalValueWithStringId_thenThrowResourceNotFoundException() throws Exception {
        // When & Then
        mockMvc.perform(get("/pedido/valor-total/abc"))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).getTotalValue(any());
    }

}
