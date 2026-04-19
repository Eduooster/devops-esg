package org.example.esg.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "CAPACIDADEPONTO_ESG")
@Data
@ToString
public class CapacidadePonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "ponto_coleta_id")
    private PontoColeta pontoColeta;
    @Enumerated(EnumType.STRING)
    private TipoMaterial tipoMaterial;
    private Integer capacidade;
    private Integer quantidadeAtual;
    @Enumerated(EnumType.STRING)
    private StatusCapacidade statusCapacidade;


}
