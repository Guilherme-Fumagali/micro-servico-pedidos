package me.gfumagali.btgpedidos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(name = "Pedido", description = "DTO para leitura de um registro de Pedido consumido da fila")
public class OrderDTO {
    @JsonProperty("codigoPedido")
    @NotNull(message = "Order code is required")
    @PositiveOrZero(message = "Order code must be positive or zero")
    @Schema(description = "Código do pedido", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long orderCode;

    @JsonProperty("codigoCliente")
    @NotNull(message = "Client code is required")
    @PositiveOrZero(message = "Client code must be positive or zero")
    @Schema(description = "Código do cliente", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long clientCode;

    @JsonProperty("itens")
    @NotNull(message = "Items are required")
    @NotEmpty(message = "Items require at least one item")
    @Valid
    @Schema(description = "Itens do pedido", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ItemDTO> items;
}
