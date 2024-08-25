package me.gfumagali.btgpedidos.repository;

import me.gfumagali.btgpedidos.repository.model.ClientOrders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientOrderRepository extends MongoRepository<ClientOrders, Long> {
}
