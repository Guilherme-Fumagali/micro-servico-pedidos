package me.gfumagali.btgpedidos.model.mappers;

import me.gfumagali.btgpedidos.model.documents.OrderTotalValue;
import me.gfumagali.btgpedidos.model.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderTotalValueMapper extends DocumentMapper<OrderTotalValue, OrderDTO> {

    @Override
    @Mapping(target = "totalValue", expression = "java(dto.getItems().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add))")
    OrderTotalValue toDocument(OrderDTO dto);
}

