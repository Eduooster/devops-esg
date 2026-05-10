package org.example.esg.application.services.pontoColetaServices;

import lombok.Builder;
import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.manualMapper.PontoColetaMapperManual;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.application.services.NominatimService;
import org.example.esg.application.services.PontoColetaService;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
public class ListarPontosColetaService {

    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapperManual pontoColetaMapperManual;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nomitinService;

    public Page<PontoColetaResponseDto> listarPontos(Pageable pageable) {
        return pontoColetaRepository.findAllBy(pageable)
                .map(ponto -> pontoColetaMapperManual.toPontoColetaResponseDtoManual(ponto));
    }


}
