package me.gfumagali.btgpedidos.model.mappers;

import me.gfumagali.btgpedidos.model.documents.Order;
import me.gfumagali.btgpedidos.model.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper extends DocumentMapper<Order, OrderDTO> {

    @Override
    @Mapping(target = "orderDate", expression = "java(LocalDateTime.now())")
    Order toDocument(OrderDTO dto);
}
