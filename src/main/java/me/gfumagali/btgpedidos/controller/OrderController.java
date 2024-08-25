package me.gfumagali.btgpedidos.controller;

import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.service.PedidoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedido")
public class OrderController {
    private final PedidoService pedidoService;

    @GetMapping("/valor-total/{id}")
    public String getTotalValue(@PathVariable Long id) {
        return pedidoService.getTotalValue(id);
    }

}
