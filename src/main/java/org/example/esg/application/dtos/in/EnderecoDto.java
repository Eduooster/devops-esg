package org.example.esg.application.dtos.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record EnderecoDto( String cep, String logradouro, String bairro, String localidade, String uf, Double lat, Double lng) { }