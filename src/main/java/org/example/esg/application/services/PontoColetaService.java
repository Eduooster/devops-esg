package org.example.esg.application.services;

import jakarta.persistence.EntityExistsException;
import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.application.dtos.in.PontoColetaRequestDto;
import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.application.dtos.out.NominatimResponse;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.domain.entities.*;
import org.example.esg.domain.exceptions.ResourceNotFoundException;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PontoColetaService {


    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapper pontoColetaMapper;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nomitinService;



    public PontoColetaService(PontoColetaRepository pontoColetaRepository, PontoColetaMapper pontoColetaMapper, EnderecoMapper enderecoMapper, NominatimService nominatimService) {
        this.pontoColetaRepository = pontoColetaRepository;
        this.pontoColetaMapper = pontoColetaMapper;
        this.enderecoMapper = enderecoMapper;
        this.nomitinService = nominatimService;
    }

    public Mono<PontoColeta> criar(PontoColetaRequestDto dto) {

        if (pontoColetaRepository.existsByNome(dto.nome())) {
            return Mono.error(new EntityExistsException("Ponto coleta já existe"));
        }
        System.out.println(dto);


        Endereco endereco = new Endereco();
        endereco.setCep(dto.endereco().cep());
        endereco.setLogradouro(dto.endereco().logradouro());
        endereco.setBairro(dto.endereco().bairro());
        endereco.setLocalidade(dto.endereco().localidade());
        endereco.setUf(dto.endereco().uf());
        endereco.setLat(dto.endereco().lat());
        endereco.setLng(dto.endereco().lng());

        System.out.println(endereco);

        PontoColeta ponto = new PontoColeta();
        ponto.setNome(dto.nome());
        ponto.setStatusPontoGeral(dto.statusPontoGeral());
        ponto.setEndereco(endereco);

        return nomitinService.buscarLatLng(endereco)
                .switchIfEmpty(Mono.fromSupplier(() -> {

                    double latAleatoria = -23.5 + Math.random() * 0.1;
                    double lngAleatoria = -46.6 + Math.random() * 0.1;
                    return new NominatimResponse(latAleatoria, lngAleatoria,null);
                }))
                .flatMap(response -> {
                    endereco.setLat(response.lat());
                    endereco.setLng(response.lon());

                    for (CapacidadePontoDto capDto : dto.capacidades()) {
                        CapacidadePonto cap = new CapacidadePonto();
                        cap.setTipoMaterial(capDto.tipoMaterial());
                        cap.setQuantidadeAtual(capDto.quantidadeAtual());
                        cap.setCapacidade(capDto.capacidade());
                        cap.setStatusCapacidade(capDto.statusCapacidade());
                        cap.setPontoColeta(ponto);
                        ponto.getCapacidades().add(cap);
                    }

                    return Mono.fromCallable(() -> pontoColetaRepository.save(ponto));
                });

    }


    public Page<PontoColetaResponseDto> listarPontos(Pageable pageable) {
        return pontoColetaRepository.findAllBy(pageable)
                .map(ponto -> toPontoColetaResponseDtoManual(ponto));
    }




    public Page<PontoColetaResponseDto> listarPontosFiltrados(
            Pageable pageable,
            StatusCapacidade statusPonto,
            String uf,
            TipoMaterial tipoMaterial) {

        Page<PontoColetaResponseDto> pagina = pontoColetaRepository
                .findByFiltros(statusPonto, uf, tipoMaterial, pageable)
                .map(ponto->toPontoColetaResponseDtoManual(ponto));

        if (pagina.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum ponto de coleta encontrado com os filtros informados.");
        }


        return pagina;
    }

    private PontoColetaResponseDto toPontoColetaResponseDtoManual(PontoColeta ponto) {
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


    public void excluir(Long id) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id).orElseThrow(()-> new EntityExistsException("Ponto coleta inexisstente"));
        pontoColeta.setDeleted(Boolean.TRUE);
        pontoColetaRepository.save(pontoColeta);
    }

    public PontoColetaResponseDto atualizarStatusPonto(Long id, StatusPontoGeral status) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id).orElseThrow(()-> new EntityExistsException("Ponto coleta inexisstente"));

        pontoColeta.setStatusPontoGeral(status);
        pontoColetaRepository.save(pontoColeta);
        return pontoColetaMapper.toPontoColetaResponseDto(pontoColeta);
    }
}
