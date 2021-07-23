package com.sample.beerstock.builder;

import com.sample.beerstock.dto.request.CervejaDTO;
import com.sample.beerstock.enums.TipoCerveja;
import lombok.Builder;

@Builder
public class CervejaDTOBuilder {
    
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Antarctica";
    
    @Builder.Default
    private String marca = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int qtde = 10;

    @Builder.Default
    private TipoCerveja tipo = TipoCerveja.PILSEN;

    public CervejaDTO toCervejaDTO() {
        return new CervejaDTO(
            id,
            name,
            marca,
            max,
            qtde,
            tipo
        );
    }
}
