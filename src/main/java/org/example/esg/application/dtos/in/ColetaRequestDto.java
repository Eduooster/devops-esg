package org.example.esg.application.dtos.in;

import org.example.esg.domain.entities.TipoMaterial;
import org.example.esg.domain.entities.Usuario;

public record ColetaRequestDto (
        Long pontoColetaId,
        Integer quantidadeDepositada,
        TipoMaterial tipoMaterial


){
}
