package org.example.esg.application.dtos.in;

public record RegisterDto(String email, String senha,String cep,String nome,EnderecoDto enderecoDto) {
}
