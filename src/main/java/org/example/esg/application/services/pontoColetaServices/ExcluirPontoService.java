package org.example.esg.application.services.pontoColetaServices;

import jakarta.persistence.EntityExistsException;
import lombok.Builder;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.application.services.NominatimService;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.stereotype.Service;

@Service
@Builder
public class ExcluirPontoService {

    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapper pontoColetaMapper;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nomitinService;


    public void excluir(Long id) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id).orElseThrow(()-> new EntityExistsException("Ponto coleta inexisstente"));
        pontoColeta.setDeleted(Boolean.TRUE);
        pontoColetaRepository.save(pontoColeta);
    }
}
