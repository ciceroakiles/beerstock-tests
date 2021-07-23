package com.sample.beerstock.service;

import com.sample.beerstock.builder.CervejaDTOBuilder;
import com.sample.beerstock.dto.request.CervejaDTO;
import com.sample.beerstock.entity.Cerveja;
import com.sample.beerstock.exception.CervLimiteQuantException;
import com.sample.beerstock.exception.CervNaoEncontException;
import com.sample.beerstock.exception.CervNomeRegException;
import com.sample.beerstock.mapper.CervejaMapper;
import com.sample.beerstock.repository.CervejaRepositorio;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    // Teste unitário (service) - criaCerveja(): novo objeto
    @Test
    void criarAoInformarObjeto() throws CervNomeRegException {
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

    // Teste unitário (service) - criaCerveja(): objeto já existente
    @Test
    void criarJaRegistrada() {
        // Construção do objeto fake e conversão
        CervejaDTO cervejaEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDuplicada = cervejaMapper.toModel(cervejaEsperada);
        // Espera-se que dê erro
        when(cervejaRepositorio.findByName(cervejaEsperada.getName()))
            .thenReturn(Optional.of(cervejaDuplicada));
        // Espera-se que lance a exceção após buscar por nome
        assertThrows(CervNomeRegException.class, () -> cervejaService.criaCerveja(cervejaEsperada));
    }

    // Teste unitário (service) - findByName(): nome do objeto é válido
    @Test
    void retornoOkAposBuscar() throws CervNaoEncontException {
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

    // Teste unitário (service) - findByName(): nome do objeto é inválido
    @Test
    void retornoFalhouAposBuscar() {
        // Construção do objeto fake
        CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Espera-se que não retorne objeto, dado o nome
        when(cervejaRepositorio.findByName(cervejaEsperadaDTO.getName()))
            .thenReturn(Optional.empty());
        // Precisa lançar a exceção
        assertThrows(CervNaoEncontException.class, () -> cervejaService.findByName(cervejaEsperadaDTO.getName()));
    }

    // Teste unitário (service) - listAll(): não vazia
    @Test
    void listaCheia() {
        // Construção do objeto fake e conversão
        CervejaDTO cervEspEncontradaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEsperada = cervejaMapper.toModel(cervEspEncontradaDTO);
        // Espera-se que retorne lista de objetos
        when(cervejaRepositorio.findAll())
            .thenReturn(Collections.singletonList(cervejaEsperada));
        // Espera-se que a lista não esteja vazia (tendo ao menos um elemento)
        List<CervejaDTO> cervejaListDTO = cervejaService.listAll();
        assertThat(cervejaListDTO, is(not(empty())));
        assertThat(cervejaListDTO.get(0), is(equalTo(cervEspEncontradaDTO)));
    }

    // Teste unitário (service) - listAll(): vazia
    @Test
    void listaVazia() {
        // Espera-se que retorne lista vazia
        when(cervejaRepositorio.findAll())
            .thenReturn(Collections.emptyList());
        // Espera-se que a lista esteja vazia
        List<CervejaDTO> cervejaListDTO = cervejaService.listAll();
        assertThat(cervejaListDTO, is(empty()));
    }

    // Teste unitário (service) - deleteById()
    @Test
    void deletarCervejaOk() throws CervNaoEncontException {
        // Construção do objeto fake e conversão
        CervejaDTO cervEspDeletadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaDeletada = cervejaMapper.toModel(cervEspDeletadaDTO);
        // Se foi possível deletar, não é preciso fazer mais nada
        when(cervejaRepositorio.findById(cervEspDeletadaDTO.getId()))
            .thenReturn(Optional.of(cervejaDeletada));
        doNothing().when(cervejaRepositorio).deleteById(cervEspDeletadaDTO.getId());
        // Espera-se que a deleção tenha sido feita (pode lançar exceção)
        cervejaService.deleteById(cervEspDeletadaDTO.getId());
        // Verifica se os métodos findById() e deleteById() foram chamados apenas uma vez
        verify(cervejaRepositorio, times(1)).findById(cervEspDeletadaDTO.getId());
        verify(cervejaRepositorio, times(1)).deleteById(cervEspDeletadaDTO.getId());
    }

    /*
     * TDD - Test Driven Development
     */
    // Aumentar estoque
    @Test
    void AumentarEstoque() throws CervNaoEncontException, CervLimiteQuantException {
        CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEsperada = cervejaMapper.toModel(cervejaEsperadaDTO);
        // Retorna Optional, salva objeto
        when(cervejaRepositorio.findById(cervejaEsperada.getId()))
            .thenReturn(Optional.of(cervejaEsperada));
        when(cervejaRepositorio.save(cervejaEsperada))
            .thenReturn(cervejaEsperada);
        int aumento = 10;
        int qtdeEsperada = cervejaEsperadaDTO.getQtde() + aumento;
        // Novo método para o service
        CervejaDTO cervAumentouDTO = cervejaService.increment(cervejaEsperadaDTO.getId(), aumento);
        assertThat(qtdeEsperada, equalTo(cervAumentouDTO.getQtde()));
        assertThat(qtdeEsperada, lessThan(cervejaEsperada.getMax()));
    }

    // Soma das quantidades não pode exceder o máximo
    @Test
    void QuantSomaExcedeMax() {
        CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        Cerveja cervejaEsperada = cervejaMapper.toModel(cervejaEsperadaDTO);
        when(cervejaRepositorio.findById(cervejaEsperada.getId()))
            .thenReturn(Optional.of(cervejaEsperada));
        int aumento = 45;
        assertThrows(CervLimiteQuantException.class, () -> cervejaService.increment(cervejaEsperada.getId(), aumento));
    }
}
