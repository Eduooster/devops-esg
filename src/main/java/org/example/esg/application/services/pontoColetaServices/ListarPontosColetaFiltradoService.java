package org.example.esg.application.services.pontoColetaServices;

import lombok.Builder;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.manualMapper.PontoColetaMapperManual;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.application.services.NominatimService;
import org.example.esg.domain.entities.StatusCapacidade;
import org.example.esg.domain.entities.TipoMaterial;
import org.example.esg.domain.exceptions.ResourceNotFoundException;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Builder
public class ListarPontosColetaFiltradoService {

    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapperManual pontoColetaMapperManual;

    public Page<PontoColetaResponseDto> listarPontosFiltrados(
            Pageable pageable,
            StatusCapacidade statusPonto,
            String uf,
            TipoMaterial tipoMaterial) {

        Page<PontoColetaResponseDto> pagina = pontoColetaRepository
                .findByFiltros(statusPonto, uf, tipoMaterial, pageable)
                .map(ponto->pontoColetaMapperManual.toPontoColetaResponseDtoManual(ponto));

        if (pagina.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum ponto de coleta encontrado com os filtros informados.");
        }


        return pagina;
    }

}
