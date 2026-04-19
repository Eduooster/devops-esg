package org.example.esg.application.dtos.out;

import org.example.esg.domain.entities.StatusCapacidade;
import org.example.esg.domain.entities.TipoMaterial;

public record CapacidadePontoDto(TipoMaterial tipoMaterial, Integer quantidadeAtual, StatusCapacidade statusCapacidade,
                                 Integer capacidade) {
}
