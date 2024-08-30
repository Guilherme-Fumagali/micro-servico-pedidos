package me.gfumagali.btgpedidos.model.documents;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.gfumagali.btgpedidos.model.dto.ItemDTO;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Schema(name = "Pedido", description = "DTO para exibição de um registro de Pedido")
public class Order {
    @JsonProperty("codigoPedido")
    @Schema(description = "Código do pedido", example = "1")
    @Indexed(unique = true)
    private Long orderCode;

    @JsonProperty("dataPedido")
    @Schema(description = "Data do pedido no formato ISO 8601", example = "2021-10-01T10:00:00")
    private LocalDateTime orderDate;

    @JsonProperty("itens")
    @Schema(description = "Itens do pedido")
    private List<ItemDTO> items;
}