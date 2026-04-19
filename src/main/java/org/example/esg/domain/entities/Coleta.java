package org.example.esg.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "COLETA_ESG")
public class Coleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    private PontoColeta pontoColeta;

    @ManyToOne
    private Usuario usuario;

    private TipoMaterial  tipoMaterial;

    private Integer quantidade;

    private LocalDateTime dataColeta;

}
