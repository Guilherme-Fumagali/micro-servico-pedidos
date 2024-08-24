package me.gfumagali.btgpedidos.listener.dto;

import lombok.Data;

@Data
public class ItemDto {
    private String produto;
    private int quantidade;
    private double preco;
}
