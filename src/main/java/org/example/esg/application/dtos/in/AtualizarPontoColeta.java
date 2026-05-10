package org.example.esg.application.dtos.in;

import org.example.esg.domain.entities.StatusPontoGeral;

public record AtualizarPontoColeta(


        String nome,

        StatusPontoGeral statusPontoGeral,

        EnderecoDto endereco
        ) {
}
