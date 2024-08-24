package me.gfumagali.btgpedidos.model.dto;

import lombok.Data;

@Data
public class ItemDTO {
    private String produto;
    private int quantidade;
    private double preco;
}
