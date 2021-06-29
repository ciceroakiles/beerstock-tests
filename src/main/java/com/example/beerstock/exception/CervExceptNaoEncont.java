package com.example.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CervExceptNaoEncont extends Exception {
    
    public CervExceptNaoEncont(Long id) {
        super("ID " + id + " não encontrado");
    }

    public CervExceptNaoEncont(String nome) {
        super("Cerveja " + nome + " não encontrada");
    }
}
