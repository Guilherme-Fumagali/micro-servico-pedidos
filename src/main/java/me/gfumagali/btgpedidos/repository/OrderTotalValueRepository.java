package me.gfumagali.btgpedidos.repository;

import me.gfumagali.btgpedidos.repository.model.OrderTotalValue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderTotalValueRepository extends MongoRepository<OrderTotalValue, Long> {
}