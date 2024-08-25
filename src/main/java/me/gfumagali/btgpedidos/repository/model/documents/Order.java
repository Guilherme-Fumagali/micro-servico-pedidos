package me.gfumagali.btgpedidos.repository.model.documents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.gfumagali.btgpedidos.listener.dto.ItemDTO;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class Order {
    @JsonProperty("codigoPedido")
    private Long orderCode;

    @JsonProperty("dataPedido")
    private LocalDateTime orderDate;

    @JsonProperty("itens")
    private List<ItemDTO> items;
}