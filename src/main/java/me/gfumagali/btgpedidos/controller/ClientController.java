package me.gfumagali.btgpedidos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cliente", description = "Recuperação de dados de compras a partir do código do cliente")
public class ClientController {
    private final OrderService orderService;

    @GetMapping(value = "/{id}/quantidade", produces = "text/plain")
    @Operation(
            summary = "Quantidade de pedidos",
            description = "Recupera a quantidade de pedidos de um cliente a partir de seu código",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quantidade de pedidos recuperada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            }
    )
    public String getOrdersQuantity(@PathVariable Long id) {
        return orderService.getOrdersQuantity(id);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            summary = "Pedidos",
            description = "Recupera os pedidos de um cliente a partir de seu código",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            }
    )
    public List<Order> getOrders(@PathVariable Long id) {
        return orderService.getOrders(id);
    }

}
