package me.gfumagali.btgpedidos.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String message = "Resource not found";

}


