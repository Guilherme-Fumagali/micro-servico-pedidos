package me.gfumagali.btgpedidos.model.mappers;


interface DocumentMapper<Document, Dto> {
    Document toDocument(Dto dto);
}
