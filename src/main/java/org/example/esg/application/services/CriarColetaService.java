package org.example.esg.application.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.esg.application.dtos.in.ColetaRequestDto;
import org.example.esg.domain.entities.*;
import org.example.esg.domain.exceptions.CapacidadePontoAtingida;
import org.example.esg.domain.exceptions.MaterialNaoSuportadoException;
import org.example.esg.domain.exceptions.PontoNaoEncontradoException;
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
        PontoColeta ponto = buscarPonto(request.pontoColetaId());
        CapacidadePonto capacidade = buscarCapacidade(ponto, request.tipoMaterial());

        int quantidadeAtualizada = calcularNovaQuantidade(capacidade, request.quantidadeDepositada());

        validarCapacidade(capacidade, quantidadeAtualizada);

        atualizarCapacidade(capacidade, quantidadeAtualizada);

        salvarColeta(request, usuario, ponto);

        verificarENotificarCapacidadeMaxima(capacidade, quantidadeAtualizada, ponto, request.tipoMaterial());
    }

    private PontoColeta buscarPonto(Long pontoId) {
        return pontoColetaRepository.findById(pontoId)
                .orElseThrow(() -> new PontoNaoEncontradoException("Ponto não encontrado"));
    }

    private CapacidadePonto buscarCapacidade(PontoColeta ponto, TipoMaterial tipoMaterial) {
        return ponto.getCapacidades().stream()
                .filter(c -> c.getTipoMaterial().equals(tipoMaterial))
                .findFirst()
                .orElseThrow(() -> new MaterialNaoSuportadoException("Material não suportado nesse ponto"));
    }

    private int calcularNovaQuantidade(CapacidadePonto capacidade, int quantidadeDepositada) {
        return capacidade.getQuantidadeAtual() + quantidadeDepositada;
    }

    private void validarCapacidade(CapacidadePonto capacidade, int quantidadeAtualizada) {
        if (quantidadeAtualizada > capacidade.getCapacidade()) {
            throw new CapacidadePontoAtingida("Capacidade excedida para esse material");
        }
    }

    private void atualizarCapacidade(CapacidadePonto capacidade, int quantidadeAtualizada) {
        capacidade.setQuantidadeAtual(quantidadeAtualizada);
    }

    private void salvarColeta(ColetaRequestDto request, Usuario usuario, PontoColeta ponto) {
        Coleta coleta = new Coleta();
        coleta.setUsuario(usuario);
        coleta.setPontoColeta(ponto);
        coleta.setTipoMaterial(request.tipoMaterial());
        coleta.setQuantidade(request.quantidadeDepositada());
        coleta.setDataColeta(LocalDateTime.now());

        coletaRepository.save(coleta);
    }

    private void verificarENotificarCapacidadeMaxima(
            CapacidadePonto capacidade,
            int quantidadeAtualizada,
            PontoColeta ponto,
            TipoMaterial tipoMaterial
    ) {
        if (atingiuCapacidadeMaxima(capacidade, quantidadeAtualizada)) {
            Notificacao notificacao = new Notificacao();
            notificacao.setMensagem("Capacidade máxima atingida para " + tipoMaterial);
            notificacao.setPontoColetaId(ponto.getId());
            notificacao.setDataHora(LocalDateTime.now());

            notificacaoRepository.save(notificacao);
        }
    }

    private boolean atingiuCapacidadeMaxima(CapacidadePonto capacidade, int quantidadeAtualizada) {
        return quantidadeAtualizada == capacidade.getCapacidade();
    }
}