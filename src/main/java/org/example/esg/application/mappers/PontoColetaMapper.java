package org.example.esg.application.mappers;

import jakarta.validation.Valid;
import org.example.esg.application.dtos.in.PontoColetaRequestDto;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.domain.entities.PontoColeta;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, CapacidadePontoMapper.class})
public interface PontoColetaMapper {
    PontoColeta toDomain(@Valid PontoColetaRequestDto request);

    PontoColetaResponseDto toResponseDto(PontoColeta pontoColeta);

    PontoColeta toDomain (PontoColetaResponseDto pontoColetaResponseDto);

    PontoColetaResponseDto toPontoColetaResponseDto(PontoColeta pontoColeta);

    PontoColetaResponseDto.PontoColetaComDistanciaDto toPontoColetaDistancia(Page<PontoColetaResponseDto> pagina);
}
