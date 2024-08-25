package me.gfumagali.btgpedidos.repository.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@AllArgsConstructor
@Document(collection = "clientOrders")
public class ClientOrders {
    @Id
    private Long clientCode;

    private Long ordersQuantity;

    private HashMap<Long, Order> orders;
}
