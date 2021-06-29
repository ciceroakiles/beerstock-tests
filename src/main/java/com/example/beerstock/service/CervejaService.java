package com.example.beerstock.service;

import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.entity.Cerveja;
import com.example.beerstock.exception.CervExceptId;
import com.example.beerstock.exception.CervExceptNomeNaoEncont;
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
    public CervejaDTO findByName(String name) throws CervExceptNomeNaoEncont {
        Cerveja encontrada = cervejaRepositorio.findByName(name)
            .orElseThrow(() -> new CervExceptNomeNaoEncont(name));
        return cervejaMapper.toDto(encontrada);
    }

    // Deleção (usa o método verificaCerveja)
    public void deleteById(Long id) throws CervExceptId {
        verificaCerveja(id);
        cervejaRepositorio.deleteById(id);
    }

    // Verifica se a cerveja existe
    private Cerveja verificaCerveja(Long id) throws CervExceptId {
        // Caso não encontre, lança a exceção (expressão lambda)
        Cerveja cervejaExiste = cervejaRepositorio.findById(id)
            .orElseThrow(() -> new CervExceptId(id));
        return cervejaExiste;
    }
    // Por nome
    private void verificaCervejaReg(String name) throws CervExceptNomeReg {
        Optional<Cerveja> jaSalva = cervejaRepositorio.findByName(name);
        if (jaSalva.isPresent()) {
            throw new CervExceptNomeReg(name);
        }
    }

    /*
    // Busca por id (usa o método verificaCerveja)
    public CervejaDTO findById(Long id) throws CervExceptId {
        return cervejaMapper.toDto(verificaCerveja(id));
    }
    // Atualização (usa os métodos verificaCerveja e criaMessageResponse)
    public MessageResponseDTO updateById(Long id, CervejaDTO cervejaDTO) throws CervExceptId {
        verificaCerveja(id);
        Cerveja updated = cervejaMapper.toModel(cervejaDTO);
        Cerveja salva = cervejaRepositorio.save(updated);
        return criaMessageResponse("Objeto <Cerveja> atualizado (ID " + salva.getId() + ")");
    }
    // Mensagem de resposta dos métodos CREATE e UPDATE
    private MessageResponseDTO criaMessageResponse(String msg) throws CervExceptId {
        return MessageResponseDTO.builder().message(msg).build();
    }
    */
}
