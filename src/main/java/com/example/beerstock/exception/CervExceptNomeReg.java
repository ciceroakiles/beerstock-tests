package com.example.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervExceptNomeReg extends Exception {
    
    public CervExceptNomeReg(String nome) {
        super("Cerveja " + nome + " já registrada");
    }
}
