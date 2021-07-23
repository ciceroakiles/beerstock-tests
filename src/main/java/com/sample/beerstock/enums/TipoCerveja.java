package com.sample.beerstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoCerveja {

    LAGER("Lager"),
    MALZBIER("Malzbier"),
    WITZBIER("Witzbier"),
    WEISS("Weiss"),
    ALE("Ale"),
    IPA("IPA"),
    STOUT("Stout"),
    PILSEN("Pilsen");

    private final String descricao;
}
