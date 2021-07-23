package com.sample.beerstock.entity;

import com.sample.beerstock.enums.TipoCerveja;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cerveja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int qtde;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCerveja tipo;
}
