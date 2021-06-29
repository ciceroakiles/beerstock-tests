package com.example.beerstock.mapper;

import com.example.beerstock.dto.request.CervejaDTO;
import com.example.beerstock.entity.Cerveja;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CervejaMapper {
    CervejaMapper INSTANCE = Mappers.getMapper(CervejaMapper.class);
    Cerveja toModel(CervejaDTO CervejaDTO);
    CervejaDTO toDto(Cerveja cerveja);
}
