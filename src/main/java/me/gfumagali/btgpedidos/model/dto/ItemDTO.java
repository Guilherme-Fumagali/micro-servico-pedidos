package me.gfumagali.btgpedidos.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ItemDTO {
    @JsonProperty("produto")
    @NotBlank(message = "Product name is required")
    private String product;

    @JsonProperty("quantidade")
    @Positive(message = "Quantity must be positive")
    @NotNull(message = "Quantity is required")
    private int quantity;

    @JsonProperty("preco")
    @PositiveOrZero(message = "Price must be positive or zero")
    @NotNull(message = "Price is required")
    private double price;
}
