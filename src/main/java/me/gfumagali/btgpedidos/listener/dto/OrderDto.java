package me.gfumagali.btgpedidos.listener.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private long codigoPedido;
    private long codigoCliente;
    private List<ItemDto> items;
}
