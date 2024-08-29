package me.gfumagali.btgpedidos.model.documents;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "clientOrders")
@Tag(name = "ClientOrders", description = "Documentos de pedidos de clientes")
public class ClientOrders {
    @Id
    private Long clientCode;

    private Integer ordersQuantity;

    private List<Order> orders;
}
