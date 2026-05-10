package org.example.esg.web.controllers;

import jakarta.validation.Valid;
import lombok.Builder;
import org.example.esg.application.dtos.in.AtualizarPontoColeta;

import org.example.esg.application.dtos.in.PontoColetaRequestDto;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.services.ListarPontosProximosService;
import org.example.esg.application.services.PontoColetaService;

import org.example.esg.application.services.pontoColetaServices.*;
import org.example.esg.domain.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestController
@RequestMapping("/ponto-coleta")
@Builder

public class PontoColetaController {

    //private final PontoColetaService pontoColetaService;
    private final ListarPontosProximosService listarPontosProximosService;
    private final AtualizarPontoColetaService atualizarPontoColetaService;
    private final CriarPontoColetaService criarPontoColetaService;
    private final ListarPontosColetaService listarPontosColetaService;
    private final ListarPontosColetaFiltradoService listarPontosColetaFiltradoService;
    private final ExcluirPontoService excluirPontoService;
    private final BuscarPontoColetaPorIdService buscarPontoColetaPorIdService;





    @PostMapping
    public ResponseEntity<PontoColetaResponseDto> criar(@RequestBody @Valid PontoColetaRequestDto request, UriComponentsBuilder uriBuilder) {


        PontoColetaResponseDto responseDto = criarPontoColetaService.criar(request);


        URI uri = uriBuilder.path("/ponto-coleta/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();


        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<PontoColetaResponseDto>> listar(@PageableDefault(size = 10,sort = {"nome"}) Pageable pageable, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(listarPontosColetaService.listarPontos(pageable));
    }

    @GetMapping("/filtrar")
    public ResponseEntity<Page<PontoColetaResponseDto>> listarFiltrados(@PageableDefault(size = 10,sort = {"nome"}) Pageable pageable,
                                                                     @RequestParam(required = false) StatusCapacidade status,
                                                                     @RequestParam(required = false) String uf,
                                                                     @RequestParam(required = false) TipoMaterial material) {

        Page<PontoColetaResponseDto> pontos = listarPontosColetaFiltradoService.listarPontosFiltrados(pageable,status, uf, material);
        return ResponseEntity.ok(pontos);

    }

    @GetMapping("/proximos")
    public ResponseEntity<Page<PontoColetaResponseDto.PontoColetaComDistanciaDto>> listarPontosProximosAtivos (@PageableDefault(size = 10,sort = {"nome"}) Pageable pageable,@AuthenticationPrincipal Usuario usuario){
            Page<PontoColetaResponseDto.PontoColetaComDistanciaDto> pontosProximos = listarPontosProximosService.listarPontosProximosAtivos(pageable,usuario);

            return ResponseEntity.ok(pontosProximos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id){
        excluirPontoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity atualizarStatusPonto( @RequestBody AtualizarPontoColeta dto ,@PathVariable Long id){
        PontoColetaResponseDto pontoAtualizado =  atualizarPontoColetaService.atualizarStatusPonto(id,dto);
        return ResponseEntity.ok(pontoAtualizado);

    }



}
