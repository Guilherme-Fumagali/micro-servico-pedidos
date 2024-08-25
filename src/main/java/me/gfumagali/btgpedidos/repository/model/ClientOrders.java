package me.gfumagali.btgpedidos.repository.model;

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
    private long codigoCliente;

    private Long quantidadePedidos;

    private HashMap<Long, Order> pedidos;
}
