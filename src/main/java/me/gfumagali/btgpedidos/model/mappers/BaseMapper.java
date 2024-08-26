package me.gfumagali.btgpedidos.model.mappers;


interface BaseMapper<E, D> {
    E toDocument(D dto);
}
