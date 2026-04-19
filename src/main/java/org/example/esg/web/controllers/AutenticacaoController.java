package org.example.esg.web.controllers;

import jakarta.validation.Valid;
import org.example.esg.application.dtos.in.DadosAuthRequestDto;
import org.example.esg.application.dtos.in.RegisterDto;
import org.example.esg.application.dtos.out.DadosTokenJwtResponseDto;
import org.example.esg.application.services.AutenticacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/auth"})
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity efetuarAutenticacao(@RequestBody @Valid DadosAuthRequestDto dados) {
        DadosTokenJwtResponseDto tokenJWT = this.autenticacaoService.autenticar(dados);
        return ResponseEntity.ok(tokenJWT);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto dados) {
        autenticacaoService.registrar(dados);
        return ResponseEntity.noContent().build();
    }
}
