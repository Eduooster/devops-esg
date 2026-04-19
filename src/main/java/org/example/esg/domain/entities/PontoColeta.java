package org.example.esg.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "PONTOCOLETA_ESG")
@ToString
public class PontoColeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;


    private String nome;
    @Enumerated(EnumType.STRING)
    private StatusPontoGeral statusPontoGeral;

    private Endereco endereco;
    @OneToMany(mappedBy = "pontoColeta", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CapacidadePonto> capacidades = new ArrayList<>();
    private Boolean deleted = Boolean.FALSE;


}
