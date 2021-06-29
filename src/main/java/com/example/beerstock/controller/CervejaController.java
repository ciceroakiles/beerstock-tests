package com.example.beerstock.controller;

import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.exception.CervExceptNaoEncont;
import com.example.beerstock.exception.CervExceptNomeReg;
import com.example.beerstock.service.CervejaService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController {

    private CervejaService cervejaService;

    // Cria cerveja
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO criaCerveja(@RequestBody @Valid CervejaDTO cervejaDTO) throws CervExceptNomeReg {
        return cervejaService.criaCerveja(cervejaDTO);
    }

    // Listagem completa
    @GetMapping
    public List<CervejaDTO> listAll() {
        return cervejaService.listAll();
    }

    // Busca cerveja por nome
    @GetMapping("/{nome}")
    public CervejaDTO findByName(@PathVariable String nome) throws CervExceptNaoEncont {
        return cervejaService.findByName(nome);
    }

    // Deleta cerveja por id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws CervExceptNaoEncont {
        cervejaService.deleteById(id);
    }
}

// Teste
@RestController
@RequestMapping("/")
class Root {
    @GetMapping
    public String msg() {
        return "Whoops! Aqui é a raiz. Tente outro caminho.";
    }
}
