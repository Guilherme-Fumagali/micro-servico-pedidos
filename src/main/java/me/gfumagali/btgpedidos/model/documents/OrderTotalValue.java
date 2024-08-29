package me.gfumagali.btgpedidos.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Document(collection = "orderTotalValue")
public class OrderTotalValue {
    @Id
    private Long orderCode;
    private BigDecimal totalValue;
}
