package me.gfumagali.btgpedidos.repository;

import me.gfumagali.btgpedidos.repository.model.documents.ClientOrders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ClientOrderRepository extends MongoRepository<ClientOrders, Long> {
    @Query(fields = "{ 'ordersQuantity' : 1 }")
    Optional<ClientOrders> getOrdersQuantityByClientCode(Long clientCode);

    @Query(fields = "{ 'orders' : 1 }")
    Optional<ClientOrders> getOrdersByClientCode(Long clientCode);
}
