package me.gfumagali.btgpedidos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(name = "Item", description = "DTO para leitura de uma unidade de produto no registro de Pedido consumido da fila")
public class ItemDTO {
    @JsonProperty("produto")
    @NotBlank(message = "Product name is required")
    @Schema(description = "Nome do produto", example = "Lápis", requiredMode = Schema.RequiredMode.REQUIRED)
    private String product;

    @JsonProperty("quantidade")
    @Positive(message = "Quantity must be positive")
    @NotNull(message = "Quantity is required")
    @Schema(description = "Quantidade do produto", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int quantity;

    @JsonProperty("preco")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 integer digits and 2 fraction digits")
    @NotNull(message = "Price is required")
    @Schema(description = "Preço do produto", example = "1.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
}
