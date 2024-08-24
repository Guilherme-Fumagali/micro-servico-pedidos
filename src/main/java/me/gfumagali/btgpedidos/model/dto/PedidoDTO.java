package me.gfumagali.btgpedidos.model.dto;

import lombok.Data;

@Data
public class PedidoDTO {
    private String codigoPedido;
    private String codigoCliente;
    private ItemDTO[] items;
}
