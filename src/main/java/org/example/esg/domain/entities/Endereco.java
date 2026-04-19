package org.example.esg.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class Endereco {
    @NonNull private String cep;
    @NonNull private String logradouro;
    @NonNull private String bairro;
    @NonNull private String localidade;
    @NonNull private String uf;

    private Double  lat;
    private Double  lng;
}
