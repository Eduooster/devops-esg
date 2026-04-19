package org.example.esg.application.dtos.in;

import java.math.BigDecimal;

public record EnderecoDto(
        String cep, String logradouro, String bairro, String localidade, String uf, Double lat, Double  lng) {
}
