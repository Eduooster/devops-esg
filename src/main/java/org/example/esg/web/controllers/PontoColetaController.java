package org.example.esg.web.controllers;

import jakarta.validation.Valid;
import org.example.esg.application.dtos.in.PontoColetaRequestDto;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.services.ListarPontosProximosService;
import org.example.esg.application.services.PontoColetaService;

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

public class PontoColetaController {

    private final PontoColetaService pontoColetaService;
    private final ListarPontosProximosService listarPontosProximosService;


    public PontoColetaController(PontoColetaService pontoColetaService, ListarPontosProximosService listarPontosProximosService) {
        this.pontoColetaService = pontoColetaService;

        this.listarPontosProximosService = listarPontosProximosService;
    }


    @PostMapping
    public ResponseEntity<PontoColetaResponseDto> criar(@RequestBody @Valid PontoColetaRequestDto request, UriComponentsBuilder uriBuilder) {
        System.out.println(request);
        Mono<PontoColeta> cadastro = pontoColetaService.criar(request);
        URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(new Object[]{cadastro.block().getId()}).toUri();
        return ResponseEntity.created(uri).build();

    }

    @GetMapping
    public ResponseEntity<Page<PontoColetaResponseDto>> listar(@PageableDefault(size = 10,sort = {"nome"}) Pageable pageable, @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(pontoColetaService.listarPontos(pageable));
    }

    @GetMapping("/filtrar")
    public ResponseEntity<Page<PontoColetaResponseDto>> listarFiltrados(@PageableDefault(size = 10,sort = {"nome"}) Pageable pageable,
                                                                     @RequestParam(required = false) StatusCapacidade status,
                                                                     @RequestParam(required = false) String uf,
                                                                     @RequestParam(required = false) TipoMaterial material) {

        Page<PontoColetaResponseDto> pontos = pontoColetaService.listarPontosFiltrados(pageable,status, uf, material);
        return ResponseEntity.ok(pontos);

    }

    @GetMapping("/proximos")
    public ResponseEntity<Page<PontoColetaResponseDto.PontoColetaComDistanciaDto>> listarPontosProximosAtivos (@PageableDefault(size = 10,sort = {"nome"}) Pageable pageable,@AuthenticationPrincipal Usuario usuario){
            Page<PontoColetaResponseDto.PontoColetaComDistanciaDto> pontosProximos = listarPontosProximosService.listarPontosProximosAtivos(pageable,usuario);

            return ResponseEntity.ok(pontosProximos);
    }

    @DeleteMapping
    public ResponseEntity excluir(@RequestParam Long id){
        pontoColetaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/status-ponto")
    public ResponseEntity atualizarStatusPonto(@RequestParam Long id,@RequestParam StatusPontoGeral status){
        PontoColetaResponseDto pontoAtualizado =  pontoColetaService.atualizarStatusPonto(id,status);
        return ResponseEntity.ok(pontoAtualizado);

    }



}
