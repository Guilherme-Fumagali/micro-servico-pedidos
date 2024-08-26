package me.gfumagali.btgpedidos.controller;

import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente/pedidos")
public class ClientController {
    private final OrderService pedidoService;

    @GetMapping("/{id}/quantidade")
    public String getOrdersQuantity(@PathVariable Long id) {
        return pedidoService.getOrdersQuantity(id);
    }

    @GetMapping("/{id}")
    public List<Order> getOrders(@PathVariable Long id) {
        return pedidoService.getOrders(id);
    }

}
