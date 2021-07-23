package com.sample.beerstock.service;

import com.sample.beerstock.dto.request.CervejaDTO;
import com.sample.beerstock.entity.Cerveja;
import com.sample.beerstock.exception.CervLimiteQuantException;
import com.sample.beerstock.exception.CervNaoEncontException;
import com.sample.beerstock.exception.CervNomeRegException;
import com.sample.beerstock.mapper.CervejaMapper;
import com.sample.beerstock.repository.CervejaRepositorio;
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
    public CervejaDTO criaCerveja(CervejaDTO cervejaDTO) throws CervNomeRegException {
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
    public CervejaDTO findByName(String name) throws CervNaoEncontException {
        Cerveja encontrada = cervejaRepositorio.findByName(name)
            .orElseThrow(() -> new CervNaoEncontException(name));
        return cervejaMapper.toDto(encontrada);
    }

    // Deleção (usa o método verificaCerveja)
    public void deleteById(Long id) throws CervNaoEncontException {
        verificaCerveja(id);
        cervejaRepositorio.deleteById(id);
    }

    // Verifica se a cerveja existe
    private Cerveja verificaCerveja(Long id) throws CervNaoEncontException {
        // Caso não encontre, lança a exceção (expressão lambda)
        Cerveja cervejaExiste = cervejaRepositorio.findById(id)
            .orElseThrow(() -> new CervNaoEncontException(id));
        return cervejaExiste;
    }
    // Por nome
    private void verificaCervejaReg(String name) throws CervNomeRegException {
        Optional<Cerveja> jaSalva = cervejaRepositorio.findByName(name);
        if (jaSalva.isPresent()) {
            throw new CervNomeRegException(name);
        }
    }

    /*
     * TDD - Método criado após AumentarEstoque()
     */
    public CervejaDTO increment(Long id, int aumento) throws CervNaoEncontException, CervLimiteQuantException {
        Cerveja cervAumento = verificaCerveja(id);
        int total = cervAumento.getQtde() + aumento;
        if (total <= cervAumento.getMax()) {
            cervAumento.setQtde(cervAumento.getQtde() + aumento);
            Cerveja cervejaSalva = cervejaRepositorio.save(cervAumento);
            return cervejaMapper.toDto(cervejaSalva);
        }
        throw new CervLimiteQuantException(id, aumento);
    }
}
