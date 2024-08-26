package me.gfumagali.btgpedidos.model.dto.listener;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemDTO {
    @JsonProperty("produto")
    private String product;

    @JsonProperty("quantidade")
    private int quantity;

    @JsonProperty("preco")
    private double price;
}
