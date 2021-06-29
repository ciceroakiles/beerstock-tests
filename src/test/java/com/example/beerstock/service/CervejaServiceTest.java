package com.example.beerstock.service;

import com.example.beerstock.builder.CervejaDTOBuilder;
import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.entity.Cerveja;
import com.example.beerstock.exception.CervExceptNaoEncont;
import com.example.beerstock.exception.CervExceptNomeReg;
import com.example.beerstock.mapper.CervejaMapper;
import com.example.beerstock.repository.CervejaRepositorio;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;

// Classe de teste (serviço)
@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {

    @Mock
    private CervejaRepositorio cervejaRepositorio;

    private CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    @InjectMocks
    private CervejaService cervejaService;

    // Teste unitário: service POST - novo objeto
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
        // Comparação de campos (os objetos precisam ser os mesmos)
        /*
        // Assert usando Mockito
        assertEquals(cervejaEsperada.getId(), cervejaCriada.getId());
        assertEquals(cervejaEsperada.getName(), cervejaCriada.getName());
        */
        // Assert usando Hamcrest e imports estáticos
        assertThat(cervejaCriada.getId(), is(equalTo(cervejaEsperada.getId())));
        assertThat(cervejaCriada.getName(), is(equalTo(cervejaEsperada.getName())));
        assertThat(cervejaCriada.getQtde(), is(equalTo(cervejaEsperada.getQtde())));
    }

    // Teste unitário: service POST - objeto já existente
    @Test
    void jaRegistrada() {
        // Construção do objeto fake e conversão
        CervejaDTO cervejaEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDuplicada = cervejaMapper.toModel(cervejaEsperada);
        // Espera-se que dê erro
        when(cervejaRepositorio.findByName(cervejaEsperada.getName()))
            .thenReturn(Optional.of(cervejaDuplicada));
        // Espera-se que lance a exceção após buscar por nome
        assertThrows(CervExceptNomeReg.class, () -> cervejaService.criaCerveja(cervejaEsperada));
    }

    // Teste unitário: service GET - nome do objeto
    @Test
    void retornaAposBuscar() throws CervExceptNaoEncont {
        // Construção do objeto fake e conversão
        CervejaDTO cervEspEncontradaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEsperada = cervejaMapper.toModel(cervEspEncontradaDTO);
        // Espera-se que retorne o objeto, dado o nome
        when(cervejaRepositorio.findByName(cervEspEncontradaDTO.getName()))
            .thenReturn(Optional.of(cervejaEsperada));
        // Pode lançar exceção caso não encontre
        CervejaDTO cervejaEncontradaDTO = cervejaService.findByName(cervEspEncontradaDTO.getName());
        // Verificação do resultado
        assertThat(cervejaEncontradaDTO, is(equalTo(cervEspEncontradaDTO)));
    }
}
