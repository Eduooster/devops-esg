package org.example.esg.application.mappers;

import lombok.NonNull;
import org.example.esg.application.dtos.in.EnderecoDto;
import org.example.esg.domain.entities.Endereco;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface EnderecoMapper {
    @NonNull Endereco toDomain(EnderecoDto enderecoDto);
}
