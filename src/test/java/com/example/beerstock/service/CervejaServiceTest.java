package com.example.beerstock.service;

import com.example.beerstock.builder.CervejaDTOBuilder;
import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.entity.Cerveja;
import com.example.beerstock.exception.CervExceptNomeReg;
import com.example.beerstock.mapper.CervejaMapper;
import com.example.beerstock.repository.CervejaRepositorio;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

// Classe de teste (serviço)
@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {

    private static final long ID_INVALIDO = 1L;

    @Mock
    private CervejaRepositorio cervejaRepositorio;

    private CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    @InjectMocks
    private CervejaService cervejaService;

    @Test
    void criarAoInformarObjeto() throws CervExceptNomeReg {
        // Construção do objeto fake e conversão
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEsperada = cervejaMapper.toModel(cervejaDTO);
        
        // Espera-se que não dê erro após buscar por nome
        when(cervejaRepositorio.findByName(cervejaDTO.getName()))
            .thenReturn(Optional.empty());
        
        // Espera-se que o objeto seja retornado quando salvo
        when(cervejaRepositorio.save(cervejaEsperada))
            .thenReturn(cervejaEsperada);
        
        // Objeto criado? Pode lançar exceção
        CervejaDTO cervejaCriada = cervejaService.criaCerveja(cervejaDTO);
        
        // Compara campos (os objetos precisam ser os mesmos)
        assertEquals(cervejaEsperada.getId(), cervejaCriada.getId());
        assertEquals(cervejaEsperada.getName(), cervejaCriada.getName());
    }
}
