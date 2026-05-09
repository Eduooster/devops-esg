package org.example.esg.web.controllers;

import jakarta.validation.Valid;
import org.example.esg.application.dtos.in.ColetaRequestDto;
import org.example.esg.application.dtos.out.ColetaResponseDto;
import org.example.esg.application.services.CriarColetaService;
import org.example.esg.domain.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/coleta")
public class ColetaController {

    private final CriarColetaService criarColetaService;

    public ColetaController(CriarColetaService criarColetaService) {
        this.criarColetaService = criarColetaService;
    }

    @PostMapping
    public ResponseEntity criar (@RequestBody @Valid ColetaRequestDto request, @AuthenticationPrincipal Usuario usuario,
                                 UriComponentsBuilder uriBuilder){

        criarColetaService.criarColeta(request,usuario);

        URI uri = uriBuilder.path("/ponto-coleta/")
                .buildAndExpand(1)
                .toUri();
        //ARRUMAR ISSOOOOOOOOOOOOOOOO
        return ResponseEntity.created(uri).build();

    }


}
