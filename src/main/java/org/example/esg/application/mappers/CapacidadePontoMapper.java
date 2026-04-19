package org.example.esg.application.mappers;

import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.domain.entities.CapacidadePonto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapacidadePontoMapper {
    CapacidadePontoDto toDomain(CapacidadePonto capacidadePonto);
}
