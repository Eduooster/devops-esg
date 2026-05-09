package org.example.esg.application.manualMapper;

import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.domain.entities.PontoColeta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PontoColetaMapperManual {

    public PontoColetaResponseDto toPontoColetaResponseDtoManual(PontoColeta ponto) {
        Endereco endereco = ponto.getEndereco();

        EnderecoDto enderecoDto = new EnderecoDto(
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getLocalidade(),
                endereco.getUf(),
                endereco.getLat(),
                endereco.getLng()
        );

        List<CapacidadePontoDto> capacidadesDto = ponto.getCapacidades().stream()
                .map(cap -> new CapacidadePontoDto(
                        cap.getTipoMaterial(),
                        cap.getQuantidadeAtual(),
                        cap.getStatusCapacidade(),
                        cap.getCapacidade()
                ))
                .toList();

        return new PontoColetaResponseDto(
                ponto.getId(),
                ponto.getNome(),
                enderecoDto,
                capacidadesDto,
                ponto.getStatusPontoGeral()
        );
    }

}
