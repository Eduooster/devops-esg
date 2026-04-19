package org.example.esg.application.dtos.out;

import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.domain.entities.StatusPontoGeral;

import java.util.List;

public record PontoColetaResponseDto(
        Long id,
        String nome,
        EnderecoDto endereco,
        List<CapacidadePontoDto> capacidades,
        StatusPontoGeral statusPontoGeral
) {

    public record PontoColetaComDistanciaDto(
            Long id,
            String nome,
            EnderecoDto endereco,
            List<CapacidadePontoDto> capacidades,
            Double distancia,
            StatusPontoGeral statusPontoGeral
    ) {
        public static PontoColetaComDistanciaDto of(PontoColetaResponseDto dto, Double distancia) {
            return new PontoColetaComDistanciaDto(
                    dto.id(),
                    dto.nome(),
                    dto.endereco(),
                    dto.capacidades(),
                    distancia,
                    dto.statusPontoGeral()
            );
        }
}}
