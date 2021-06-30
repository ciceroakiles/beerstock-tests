package com.example.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervLimiteQuantException extends Exception {

    public CervLimiteQuantException(Long id, int aumento) {
        super("Qtde. informada (" + aumento + ")  excede a cap. máxima para a cerveja escolhida (ID " + id + ")");
    }
}
