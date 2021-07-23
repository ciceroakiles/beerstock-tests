package com.sample.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CervNaoEncontException extends Exception {
    
    public CervNaoEncontException(Long id) {
        super("ID " + id + " não encontrado");
    }

    public CervNaoEncontException(String nome) {
        super("Cerveja " + nome + " não encontrada");
    }
}
