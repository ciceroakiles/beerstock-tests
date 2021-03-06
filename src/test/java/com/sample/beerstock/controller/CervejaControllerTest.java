package com.sample.beerstock.controller;

import com.sample.beerstock.builder.CervejaDTOBuilder;
import com.sample.beerstock.dto.request.CervejaDTO;
import com.sample.beerstock.dto.request.QuantidadeDTO;
import com.sample.beerstock.exception.CervNaoEncontException;
import com.sample.beerstock.service.CervejaService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import static com.sample.beerstock.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Classe de teste (controller)
@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {
    
    private static final String CAMINHO = "/api/v1/cervejas";
    private static final long VALID_ID = 1L;
    private static final long INVALID_ID = 2L;
    private static final String AUMENTAR = "/mais";
    //private static final String REDUZIR = "/menos";

    private MockMvc mockMvc;

    @Mock
    private CervejaService cervejaService;

    @InjectMocks
    public CervejaController cervejaController;

    // Setup do MockMvc chamado antes de cada teste
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
            .build();
    }

    // Teste unit??rio (controller) - POST: normal
    @Test
    void PostCriaCerveja() throws Exception {
        // Objeto fake passa pelo servi??o
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        when(cervejaService.criaCerveja(cervejaDTO))
            .thenReturn(cervejaDTO);
        // Mock do m??todo POST (imports est??ticos)
        // Pode lan??ar exce????o
        mockMvc.perform(
            // Passando em formato JSON
            post(CAMINHO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO))
            )
            .andExpect(status().isCreated())
            // Valida????o de campos
            .andExpect(jsonPath("$.name", is(cervejaDTO.getName())))
            .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
            .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    // Teste unit??rio (controller) - POST: campo nulo
    @Test
    void PostCriaCervejaSemCampo() throws Exception {
        // Objeto fake criado, mas sem um campo
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setMarca(null);
        // Mock do m??todo POST (imports est??ticos)
        mockMvc.perform(
            post(CAMINHO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO))
            )
            // "Bad Request" esperado
            .andExpect(status().isBadRequest());
    }

    // Teste unit??rio (controller) - GET: nome do objeto ?? v??lido
    @Test
    void GetNomeOk() throws Exception {
        // Objeto fake
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Retorna o objeto (pode lan??ar exce????o)
        when(cervejaService.findByName(cervejaDTO.getName()))
            .thenReturn(cervejaDTO);
        // Mock do m??todo GET (imports est??ticos)
        mockMvc.perform(
                get(CAMINHO + "/" + cervejaDTO.getName())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            // Valida????o de campos
            .andExpect(jsonPath("$.name", is(cervejaDTO.getName())))
            .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
            .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    // Teste unit??rio (controller) - GET: nome do objeto n??o registrado
    @Test
    void GetNomeNaoEncontrado() throws Exception {
        // Objeto fake
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Lan??a exce????o
        when(cervejaService.findByName(cervejaDTO.getName()))
            .thenThrow(CervNaoEncontException.class);
        // Mock do m??todo GET (imports est??ticos)
        mockMvc.perform(
                get(CAMINHO + "/" + cervejaDTO.getName())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            // "Not Found" esperado
            .andExpect(status().isNotFound());
    }

    // Teste unit??rio (controller) - GET: lista
    @Test
    void GetListAll() throws Exception {
        // Objeto fake
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Retorna lista de objetos (pode lan??ar exce????o)
        when(cervejaService.listAll())
            .thenReturn(Collections.singletonList(cervejaDTO));
        // Mock do m??todo GET (imports est??ticos)
        mockMvc.perform(
                get(CAMINHO)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            // Valida????o de campos
            .andExpect(jsonPath("$[0].name", is(cervejaDTO.getName())))
            .andExpect(jsonPath("$[0].marca", is(cervejaDTO.getMarca())))
            .andExpect(jsonPath("$[0].tipo", is(cervejaDTO.getTipo().toString())));
    }

    // Teste unit??rio (controller) - GET: lista vazia
    @Test
    void GetEmptyList() throws Exception {
        // Retorna lista de objetos (pode lan??ar exce????o)
        when(cervejaService.listAll())
            .thenReturn(Collections.emptyList());
        // Mock do m??todo GET (imports est??ticos)
        mockMvc.perform(
                get(CAMINHO)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    // Teste unit??rio (controller) - DELETE: nome do objeto ?? v??lido
    @Test
    void DeleteNomeOk() throws Exception {
        // Objeto fake
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Se foi poss??vel deletar, n??o ?? preciso fazer mais nada
        doNothing().when(cervejaService).deleteById(cervejaDTO.getId());
        // Mock do m??todo DELETE (imports est??ticos)
        mockMvc.perform(
                delete(CAMINHO + "/" + cervejaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            // "No Content" esperado
            .andExpect(status().isNoContent());
    }

    // Teste unit??rio (controller) - DELETE: nome do objeto n??o registrado
    @Test
    void DeleteNomeNaoEncontrado() throws Exception {
        // Lan??a exce????o
        doThrow(CervNaoEncontException.class).when(cervejaService).deleteById(INVALID_ID);
        // Mock do m??todo DELETE (imports est??ticos)
        mockMvc.perform(
                delete(CAMINHO + "/" + INVALID_ID)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            // "Not Found" esperado
            .andExpect(status().isNotFound());
    }

    /*
     * TDD - Test Driven Development
     */
    // PATCH: aumentar quantidade
    @Test
    void PatchAumentaQuantidade() throws Exception {
        // Cria????o dos objetos
        QuantidadeDTO qtdeDTO = QuantidadeDTO.builder().qtde(10).build();
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQtde(cervejaDTO.getQtde() + qtdeDTO.getQtde());
        // Poss??vel lan??amento de exce????o
        when(cervejaService.increment(VALID_ID, qtdeDTO.getQtde()))
            .thenReturn(cervejaDTO);
        // Realiza????o do PATCH
        mockMvc.perform(
                patch(CAMINHO + "/" + VALID_ID + AUMENTAR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(qtdeDTO))
            )
            .andExpect(status().isOk())
            // Valida????o de campos
            .andExpect(jsonPath("$.name", is(cervejaDTO.getName())))
            .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
            .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())))
            .andExpect(jsonPath("$.qtde", is(cervejaDTO.getQtde())));
    }
}
