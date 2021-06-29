package com.example.beerstock.controller;

import com.example.beerstock.builder.CervejaDTOBuilder;
import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.exception.CervExceptNaoEncont;
import com.example.beerstock.service.CervejaService;
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
import static com.example.beerstock.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private static final String REDUZIR = "/menos";

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

    // Teste unitário (controller) - POST: normal
    @Test
    void PostCriaCerveja() throws Exception {
        // Objeto fake passa pelo serviço
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        when(cervejaService.criaCerveja(cervejaDTO))
            .thenReturn(cervejaDTO);
        // Mock do método POST (imports estáticos)
        // Pode lançar exceção
        mockMvc.perform(
            // Passando em formato JSON
            post(CAMINHO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO))
            )
            .andExpect(status().isCreated())
            // Validação de campos
            .andExpect(jsonPath("$.name", is(cervejaDTO.getName())))
            .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
            .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    // Teste unitário (controller) - POST: campo nulo
    @Test
    void PostCriaCervejaSemCampo() throws Exception {
        // Objeto fake criado, mas sem um campo
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setMarca(null);
        // Mock do método POST (imports estáticos)
        mockMvc.perform(
            post(CAMINHO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cervejaDTO))
            )
            // "Bad Request" esperado
            .andExpect(status().isBadRequest());
    }

    // Teste unitário (controller) - GET: nome do objeto é válido
    @Test
    void GetNomeOk() throws Exception {
        // Objeto fake
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Retorna o objeto (pode lançar exceção)
        when(cervejaService.findByName(cervejaDTO.getName()))
            .thenReturn(cervejaDTO);
        // Mock do método GET (imports estáticos)
        mockMvc.perform(
                get(CAMINHO + "/" + cervejaDTO.getName())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            // Validação de campos
            .andExpect(jsonPath("$.name", is(cervejaDTO.getName())))
            .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
            .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    // Teste unitário (controller) - GET: nome do objeto não registrado
    @Test
    void GetNomeNaoEncontrado() throws Exception {
        // Objeto fake
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        // Lança exceção
        when(cervejaService.findByName(cervejaDTO.getName()))
            .thenThrow(CervExceptNaoEncont.class);
        // Mock do método GET (imports estáticos)
        mockMvc.perform(
                get(CAMINHO + "/" + cervejaDTO.getName())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            // "Not Found" esperado
            .andExpect(status().isNotFound());
    }
}
