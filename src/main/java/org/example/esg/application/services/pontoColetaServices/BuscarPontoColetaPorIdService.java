package org.example.esg.application.services.pontoColetaServices;

import lombok.Builder;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.application.services.NominatimService;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.domain.exceptions.PontoNaoEncontradoException;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.stereotype.Service;

@Builder
@Service
public class BuscarPontoColetaPorIdService {

    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapper pontoColetaMapper;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nomitinService;



    private PontoColeta buscarPonto(Long pontoId) {
        return pontoColetaRepository.findById(pontoId)
                .orElseThrow(() -> new PontoNaoEncontradoException("Ponto não encontrado"));
    }
}
