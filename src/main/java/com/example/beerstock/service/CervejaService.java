package com.example.beerstock.service;

import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.entity.Cerveja;
import com.example.beerstock.exception.CervExceptNaoEncont;
import com.example.beerstock.exception.CervExceptNomeReg;
import com.example.beerstock.mapper.CervejaMapper;
import com.example.beerstock.repository.CervejaRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CervejaService {
    private CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;
    private CervejaRepositorio cervejaRepositorio;

    @Autowired
    public CervejaService(CervejaRepositorio cervejaRepositorio) {
        this.cervejaRepositorio = cervejaRepositorio;
    }

    // Inserção de nova cerveja
    public CervejaDTO criaCerveja(CervejaDTO cervejaDTO) throws CervExceptNomeReg {
        verificaCervejaReg(cervejaDTO.getName());
        Cerveja beforeSave = cervejaMapper.toModel(cervejaDTO);
        Cerveja salva = cervejaRepositorio.save(beforeSave);
        return cervejaMapper.toDto(salva);
    }

    // Converte cada objeto e retorna a lista DTO inteira
    public List<CervejaDTO> listAll() {
        List<Cerveja> todasCervejas = cervejaRepositorio.findAll();
        return todasCervejas
            .stream()
            .map(cervejaMapper::toDto)
            .collect(Collectors.toList());
    }

    // Busca por nome
    public CervejaDTO findByName(String name) throws CervExceptNaoEncont {
        Cerveja encontrada = cervejaRepositorio.findByName(name)
            .orElseThrow(() -> new CervExceptNaoEncont(name));
        return cervejaMapper.toDto(encontrada);
    }

    // Deleção (usa o método verificaCerveja)
    public void deleteById(Long id) throws CervExceptNaoEncont {
        verificaCerveja(id);
        cervejaRepositorio.deleteById(id);
    }

    // Verifica se a cerveja existe
    private Cerveja verificaCerveja(Long id) throws CervExceptNaoEncont {
        // Caso não encontre, lança a exceção (expressão lambda)
        Cerveja cervejaExiste = cervejaRepositorio.findById(id)
            .orElseThrow(() -> new CervExceptNaoEncont(id));
        return cervejaExiste;
    }
    // Por nome
    private void verificaCervejaReg(String name) throws CervExceptNomeReg {
        Optional<Cerveja> jaSalva = cervejaRepositorio.findByName(name);
        if (jaSalva.isPresent()) {
            throw new CervExceptNomeReg(name);
        }
    }
}
