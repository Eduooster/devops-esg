package org.example.esg.application.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.esg.application.dtos.in.ColetaRequestDto;
import org.example.esg.domain.entities.*;
import org.example.esg.infra.persistence.CapacidadePontoRepository;
import org.example.esg.infra.persistence.ColetaRepository;
import org.example.esg.infra.persistence.NotificacaoRepository;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service

public class CriarColetaService {

    private final PontoColetaRepository pontoColetaRepository;

    private  final ColetaRepository coletaRepository;
    private final NotificacaoRepository notificacaoRepository;


    public CriarColetaService( PontoColetaRepository pontoColetaRepository,  ColetaRepository coletaRepository, NotificacaoRepository notificacaoRepository) {
        this.pontoColetaRepository = pontoColetaRepository;

        this.coletaRepository = coletaRepository;
        this.notificacaoRepository = notificacaoRepository;
    }



    public void criarColeta(ColetaRequestDto request, Usuario usuario) {
        PontoColeta ponto = pontoColetaRepository.findById(request.pontoColetaId())
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));

        CapacidadePonto capacidade = ponto.getCapacidades().stream()
                .filter(c -> c.getTipoMaterial().equals(c.getTipoMaterial()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Material não suportado nesse ponto"));


        Integer novaQuantidade = capacidade.getQuantidadeAtual() + request.quantidadeDepositada();

        if (novaQuantidade > capacidade.getCapacidade()) {
            throw new RuntimeException("Capacidade excedida para esse material");
        }
        capacidade.setQuantidadeAtual(novaQuantidade);

        Coleta coleta = new Coleta();
        coleta.setUsuario(usuario);
        coleta.setPontoColeta(ponto);
        coleta.setTipoMaterial(request.tipoMaterial());
        coleta.setQuantidade(request.quantidadeDepositada());
        coleta.setDataColeta(LocalDateTime.now());

        coletaRepository.save(coleta);

        if (novaQuantidade.equals(capacidade.getCapacidade())) {
            Notificacao notificacao = new Notificacao();
            notificacao.setMensagem("Capacidade máxima atingida para " + request.tipoMaterial());
            notificacao.setPontoColetaId(ponto.getId());
            notificacao.setDataHora(LocalDateTime.now());

            notificacaoRepository.save(notificacao);
        }

    }
}
