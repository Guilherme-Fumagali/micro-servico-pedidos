package me.gfumagali.btgpedidos.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "orderTotalValue")
public class OrderTotalValue {
    @Id
    private long codigoPedido;
    private double valorTotal;
}
