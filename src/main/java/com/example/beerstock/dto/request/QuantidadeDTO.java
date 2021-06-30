package com.example.beerstock.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuantidadeDTO {
    
    @NotNull
    @Max(100)
    private Integer qtde;
}
