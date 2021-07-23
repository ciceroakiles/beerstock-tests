package com.sample.beerstock.mapper;

import com.sample.beerstock.dto.request.CervejaDTO;
import com.sample.beerstock.entity.Cerveja;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CervejaMapper {
    CervejaMapper INSTANCE = Mappers.getMapper(CervejaMapper.class);
    Cerveja toModel(CervejaDTO CervejaDTO);
    CervejaDTO toDto(Cerveja cerveja);
}
