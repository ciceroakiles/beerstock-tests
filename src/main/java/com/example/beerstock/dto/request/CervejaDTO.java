package com.example.beerstock.dto.request;

import com.example.beerstock.enums.TipoCerveja;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CervejaDTO {
    
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 200)
    private String marca;

    @NotNull
    @Max(500)
    private int max;

    @NotNull
    @Max(100)
    private int qtde;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCerveja tipo;
}
