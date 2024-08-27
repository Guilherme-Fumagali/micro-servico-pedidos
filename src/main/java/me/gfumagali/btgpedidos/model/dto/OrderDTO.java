package me.gfumagali.btgpedidos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty("codigoPedido")
    @NotNull(message = "Order code is required")
    @PositiveOrZero(message = "Order code must be positive or zero")
    private long orderCode;

    @JsonProperty("codigoCliente")
    @NotNull(message = "Client code is required")
    @PositiveOrZero(message = "Client code must be positive or zero")
    private long clientCode;

    @JsonProperty("itens")
    @NotNull(message = "Items are required")
    @NotEmpty(message = "Items require at least one item")
    @Valid
    private List<ItemDTO> items;
}
