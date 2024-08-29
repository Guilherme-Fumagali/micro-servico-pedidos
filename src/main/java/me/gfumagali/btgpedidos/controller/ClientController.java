package me.gfumagali.btgpedidos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente/pedidos")
@Tag(name = "Cliente", description = "Recuperação de dados de compras a partir do código do cliente")
public class ClientController {
    private final OrderService orderService;

    @GetMapping(value = "/{codigoCliente}/quantidade", produces = "text/plain")
    @Operation(
            summary = "Quantidade de pedidos",
            description = "Recupera a quantidade de pedidos de um cliente a partir de seu código",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quantidade de pedidos recuperada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            }
    )
    public String getOrdersQuantity(@PathVariable Long codigoCliente) {
        return orderService.getOrdersQuantity(codigoCliente);
    }

    @GetMapping(value = "/{codigoCliente}", produces = "application/json")
    @Operation(
            summary = "Pedidos",
            description = "Recupera os pedidos de um cliente a partir de seu código",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            }
    )
    public Page<Order> getOrders(@PathVariable Long codigoCliente,
                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") @Max(value = 100, message = "O tamanho máximo permitido da página é 100") Integer size
    ) {
        return orderService.getOrders(codigoCliente, page, size);
    }

}
