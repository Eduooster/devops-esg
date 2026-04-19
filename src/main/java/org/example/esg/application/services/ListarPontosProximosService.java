package org.example.esg.application.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.application.dtos.out.CapacidadePontoDto;
import org.example.esg.application.dtos.out.NominatimResponse;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.domain.entities.StatusCapacidade;
import org.example.esg.domain.entities.Usuario;
import org.example.esg.domain.exceptions.NominatimFailSearch;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.example.esg.infra.persistence.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class ListarPontosProximosService {
    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapper pontoColetaMapper;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nominatimService;
    private static final double RAIO_TERRA_KM = 6371.0;

    public ListarPontosProximosService(PontoColetaRepository pontoColetaRepository, PontoColetaMapper pontoColetaMapper, UsuarioRepository usuarioRepository, EnderecoMapper enderecoMapper, NominatimService nominatimService) {
        this.pontoColetaRepository = pontoColetaRepository;
        this.pontoColetaMapper = pontoColetaMapper;
        this.usuarioRepository = usuarioRepository;
        this.enderecoMapper = enderecoMapper;

        this.nominatimService = nominatimService;
    }

    public Page<PontoColetaResponseDto.PontoColetaComDistanciaDto> listarPontosProximosAtivos(Pageable pageable, Usuario usuario) {

        Optional<Usuario> usuarioRepo = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioRepo.isEmpty()){
            throw new EntityNotFoundException("Usuario não encontrado");
        }

        List<StatusCapacidade> statusValidos = List.of(StatusCapacidade.ATIVO, StatusCapacidade.AGUARDANDO_COLETA);


        Page<PontoColetaResponseDto> pagina = pontoColetaRepository
                .findDistinctByEndereco_UfAndCapacidades_statusCapacidadeIn(usuario.getEndereco().getUf(), statusValidos, pageable)
                .map(ponto -> {


                    PontoColetaResponseDto dto = toPontoColetaResponseDtoManual(ponto);


                    List<CapacidadePontoDto> capacidadesFiltradas = dto.capacidades().stream()
                            .filter(cap -> statusValidos.contains(cap.statusCapacidade()))
                            .toList();


                    return new PontoColetaResponseDto(
                            dto.id(),
                            dto.nome(),
                            dto.endereco(),
                            capacidadesFiltradas,
                            dto.statusPontoGeral()

                    );
                });

        verificarCoordenadasUsuario();

        Mono<NominatimResponse> coordenadasUsuarioMono;

        if (usuario.getEndereco().getLat() != null && usuario.getEndereco().getLng() != null) {
            coordenadasUsuarioMono = Mono.just(new NominatimResponse(usuarioRepo.get().getEndereco().getLat(), usuarioRepo.get().getEndereco().getLng(),null));
        } else {

            coordenadasUsuarioMono = nominatimService.buscarLatLng(usuarioRepo.get().getEndereco())
                    .switchIfEmpty(Mono.error(new NominatimFailSearch("Não foi possível obter coordenadas do usuário")));
            System.out.println(coordenadasUsuarioMono.block());
            usuarioRepo.get().getEndereco().setLat(coordenadasUsuarioMono.block().lat());
            usuarioRepo.get().getEndereco().setLng(coordenadasUsuarioMono.block().lon());
            usuarioRepo.ifPresent(usuarioRepository::save);


        }

        Page<PontoColetaResponseDto.PontoColetaComDistanciaDto> pontosMaisProximos = buscarDoisPontosMaisProximos(
                pagina, coordenadasUsuarioMono.block().lat(),coordenadasUsuarioMono.block().lon(), pageable);

        return pontosMaisProximos;



    }

    private void verificarCoordenadasUsuario() {
    }

    public Page<PontoColetaResponseDto.PontoColetaComDistanciaDto> buscarDoisPontosMaisProximos(
            Page<PontoColetaResponseDto> pagina,
            Double latUsuario,
            Double lonUsuario,
            Pageable pageable) {

        List<PontoColetaResponseDto> doisMaisProximos = pegarDoisPontosColetaMaisProximos(
                pagina,
                 latUsuario,
                 lonUsuario,
                 pageable);


        var pontoColetaComDistancia = comDistancia(doisMaisProximos,latUsuario,lonUsuario);

        return new PageImpl<>(pontoColetaComDistancia, pageable, pontoColetaComDistancia.size());
    }

    private List<PontoColetaResponseDto.PontoColetaComDistanciaDto> comDistancia(List<PontoColetaResponseDto> doisMaisProximos,Double latUsuario,Double lonUsuario) {
        List<PontoColetaResponseDto.PontoColetaComDistanciaDto> comDistancias = doisMaisProximos.stream()
                .map(ponto -> {
                    Double distancia = calcularDistancia(
                            ponto.endereco().lat(),
                            ponto.endereco().lng(),
                            latUsuario,
                            lonUsuario);

                    return PontoColetaResponseDto.PontoColetaComDistanciaDto.of(ponto, distancia);
                })
                .toList();
        return comDistancias;
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




    public List<PontoColetaResponseDto> pegarDoisPontosColetaMaisProximos(
            Page<PontoColetaResponseDto> pagina,
            Double latUsuario,
            Double lonUsuario,
            Pageable pageable
    ){
        List<PontoColetaResponseDto> doisMaisProximos = pagina.stream()
                .sorted(Comparator.comparing(ponto ->
                        calcularDistancia(
                                ponto.endereco().lat(),
                                ponto.endereco().lng(),
                                latUsuario,
                                lonUsuario)))
                .limit(2)
                .toList();

        return doisMaisProximos;

    }

    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        System.out.println(lat1 + " " + lon1);
        System.out.println(lat2 + " " + lon2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAIO_TERRA_KM * c;
    }



}
