package me.gfumagali.btgpedidos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedido")
@Tag(name = "Pedido", description = "Recuperação de dados de pedidos")
public class OrderController {
    private final OrderService orderService;

    @GetMapping(value = "/valor-total/{id}", produces = "text/plain")
    @Operation(
            summary = "Valor total",
            description = "Recupera o valor total de um pedido a partir de seu código",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Valor total recuperado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            }
    )
    public String getTotalValue(@PathVariable Long id) {
        return orderService.getTotalValue(id);
    }

}
