package me.gfumagali.btgpedidos.repository;

import me.gfumagali.btgpedidos.repository.model.ClientOrders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ClientOrderRepository extends MongoRepository<ClientOrders, Long> {
    @Query(fields = "{ 'quantidadePedidos' : 1 }")
    Optional<ClientOrders> getQuantidadePedidosByCodigoCliente(Long id);

    @Query(fields = "{ 'pedidos' : 1 }")
    Optional<ClientOrders> getPedidosByCodigoCliente(Long id);
}
