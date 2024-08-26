package me.gfumagali.btgpedidos.model.mappers;

import me.gfumagali.btgpedidos.model.documents.OrderTotalValue;
import me.gfumagali.btgpedidos.model.dto.listener.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderTotalValueMapper extends BaseMapper<OrderTotalValue, OrderDTO> {

    @Override
    @Mapping(target = "orderCode", source = "orderCode")
    @Mapping(target = "totalValue", expression = "java(dto.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum())")
    OrderTotalValue toDocument(OrderDTO dto);
}
