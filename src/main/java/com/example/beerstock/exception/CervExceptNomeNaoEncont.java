package com.example.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CervExceptNomeNaoEncont extends Exception {
    
    public CervExceptNomeNaoEncont(String nome) {
        super("Cerveja " + nome + " não encontrada");
    }
}
