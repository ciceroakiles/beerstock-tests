package com.example.beerstock.builder;

import com.example.beerstock.dto.request.QuantidadeDTO;
import lombok.Builder;

@Builder
public class QuantidadeDTOBuilder {
    
    @Builder.Default
    private int qtde = 0;

    public QuantidadeDTO quantidade(int q) {
        return new QuantidadeDTO(q);
    }
}
