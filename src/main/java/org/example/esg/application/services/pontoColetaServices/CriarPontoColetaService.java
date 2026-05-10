package org.example.esg.application.services.pontoColetaServices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.application.dtos.in.PontoColetaRequestDto;
import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.application.dtos.out.NominatimResponse;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.manualMapper.PontoColetaMapperManual;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.application.services.NominatimService;
import org.example.esg.domain.entities.CapacidadePonto;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.domain.exceptions.PontoNaoEncontradoException;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.stereotype.Service;

@Service
@Builder
public class CriarPontoColetaService {
    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapper pontoColetaMapper;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nomitinService;
    private final PontoColetaMapperManual pontoColetaMapperManual;

    public PontoColetaResponseDto criar(PontoColetaRequestDto dto) {

        if (pontoColetaRepository.existsByNome(dto.nome())) {
            throw new PontoNaoEncontradoException("Ponto com este nome já cadastrado");
        }

        Endereco endereco = new Endereco();
        endereco.setCep(dto.endereco().cep());
        endereco.setLogradouro(dto.endereco().logradouro());
        endereco.setBairro(dto.endereco().bairro());
        endereco.setLocalidade(dto.endereco().localidade());
        endereco.setUf(dto.endereco().uf());


        NominatimResponse response = nomitinService.buscarLatLng(endereco);


        if (response == null) {
            double latAleatoria = -23.5 + Math.random() * 0.1;
            double lngAleatoria = -46.6 + Math.random() * 0.1;
            endereco.setLat(latAleatoria);
            endereco.setLng(lngAleatoria);
        } else {
            endereco.setLat(response.lat());
            endereco.setLng(response.lon());
        }

        PontoColeta ponto = new PontoColeta();
        ponto.setNome(dto.nome());
        ponto.setStatusPontoGeral(dto.statusPontoGeral());
        ponto.setEndereco(endereco);


        for (CapacidadePontoDto capDto : dto.capacidades()) {
            CapacidadePonto cap = new CapacidadePonto();
            cap.setTipoMaterial(capDto.tipoMaterial());
            cap.setQuantidadeAtual(capDto.quantidadeAtual());
            cap.setCapacidade(capDto.capacidade());
            cap.setStatusCapacidade(capDto.statusCapacidade());
            cap.setPontoColeta(ponto);
            ponto.getCapacidades().add(cap);
        }


        PontoColeta salvo = pontoColetaRepository.save(ponto);


        return new PontoColetaResponseDto(
                salvo.getId(),
                salvo.getNome(),
                new EnderecoDto(
                        salvo.getEndereco().getCep(),
                        salvo.getEndereco().getLogradouro(),
                        salvo.getEndereco().getBairro(),
                        salvo.getEndereco().getLocalidade(),
                        salvo.getEndereco().getUf(),
                        salvo.getEndereco().getLat(),
                        salvo.getEndereco().getLng()
                ),
                salvo.getCapacidades().stream()
                        .map(cap -> new CapacidadePontoDto(
                                cap.getTipoMaterial(),
                                cap.getQuantidadeAtual(),
                                cap.getStatusCapacidade(),
                                cap.getCapacidade()
                        ))
                        .toList(),
                salvo.getStatusPontoGeral()
        );

    }


}
