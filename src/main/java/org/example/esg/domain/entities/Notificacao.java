package org.example.esg.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "NOTIFICACAO_ESG")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private Long pontoColetaId;
    private String mensagem;
    private LocalDateTime dataHora;
    private String status;
}
