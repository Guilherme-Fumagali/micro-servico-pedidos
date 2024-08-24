package me.gfumagali.btgpedidos.repository.model;

import me.gfumagali.btgpedidos.listener.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        String codigoPedido,
        LocalDateTime dataPedido,
        List<ItemDto> items) {
}
