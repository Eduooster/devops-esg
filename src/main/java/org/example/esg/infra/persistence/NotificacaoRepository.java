package org.example.esg.infra.persistence;

import org.example.esg.domain.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
}
