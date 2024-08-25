package me.gfumagali.btgpedidos.listener.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    @JsonProperty("codigoPedido")
    private long orderCode;

    @JsonProperty("codigoCliente")
    private long clientCode;

    @JsonProperty("itens")
    private List<ItemDTO> items;
}
