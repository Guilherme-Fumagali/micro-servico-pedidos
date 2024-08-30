package me.gfumagali.btgpedidos.repository;

import me.gfumagali.btgpedidos.model.documents.ClientOrders;
import me.gfumagali.btgpedidos.model.documents.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface ClientOrderRepository extends MongoRepository<ClientOrders, Long> {
    @Query(fields = "{ 'ordersQuantity' : 1 }")
    Optional<ClientOrders> getOrdersQuantityByClientCode(Long clientCode);

    @Query(fields = "{ 'orders' : { '$slice' : [?1, ?2] }, 'ordersQuantity' : 1 }", value = "{ 'clientCode' : ?0 }")
    Optional<ClientOrders> getOrdersByClientCode(Long clientCode, Integer skip, Integer limit);

    @Query(fields = "{ '_id' : 1 }")
    Optional<ClientOrders> findByOrdersOrderCode(Long orderCode);

    @Query("{ 'clientCode' : ?0}")
    @Update("{ '$push' : { orders : ?1 }, '$inc' : { ordersQuantity : 1 } }")
    void createOrder(Long clientCode, Order order);

    @Query("{ 'clientCode' : ?0, 'orders.orderCode' : ?1 }")
    @Update("{ '$set' : { 'orders.$' : ?2 } }")
    void replaceOrder(Long clientCode, Long orderCode, Order order);
}
