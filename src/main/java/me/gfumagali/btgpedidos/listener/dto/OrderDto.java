package me.gfumagali.btgpedidos.listener.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private int codigoPedido;
    private int codigoCliente;
    private List<ItemDto> items;
}
