package com.example.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CervExceptId extends Exception {
    
    public CervExceptId(Long id) {
        super("ID " + id + " não encontrado");
    }
}
