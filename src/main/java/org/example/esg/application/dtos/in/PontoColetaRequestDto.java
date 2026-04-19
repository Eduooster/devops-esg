package org.example.esg.application.dtos.in;

import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.domain.entities.StatusPontoGeral;

import java.util.List;

public record PontoColetaRequestDto(
          String nome,
          EnderecoDto endereco,
          List<CapacidadePontoDto> capacidades,
          StatusPontoGeral statusPontoGeral
          ) {
}
