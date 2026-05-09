package org.example.esg.application.services.pontoColetaServices;

import jakarta.persistence.EntityExistsException;
import lombok.Builder;
import org.example.esg.application.dtos.out.PontoColetaResponseDto;
import org.example.esg.application.manualMapper.PontoColetaMapperManual;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.application.mappers.PontoColetaMapper;
import org.example.esg.application.services.NominatimService;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.springframework.stereotype.Service;

@Service
@Builder
public class AtualizarPontoColetaService {

    private final PontoColetaRepository pontoColetaRepository;
    private final PontoColetaMapper pontoColetaMapper;
    private final EnderecoMapper enderecoMapper;
    private final NominatimService nomitinService;
    private final PontoColetaMapperManual pontoColetaMapperManual;


    public PontoColetaResponseDto atualizarStatusPonto(Long id, org.example.esg.application.dtos.in.AtualizarPontoColeta dto) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id).orElseThrow(()-> new EntityExistsException("Ponto coleta inexistente"));

        atualizarCampos(pontoColeta,dto);
        pontoColetaRepository.save(pontoColeta);

        return pontoColetaMapperManual.toPontoColetaResponseDtoManual(pontoColeta);
    }

    private void atualizarCampos(PontoColeta entidade, org.example.esg.application.dtos.in.AtualizarPontoColeta dto) {

        if (dto.nome() != null) {
            entidade.setNome(dto.nome());
        }

        if (dto.statusPontoGeral() != null) {
            entidade.setStatusPontoGeral(dto.statusPontoGeral());
        }

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(dto.endereco().logradouro());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setLocalidade(dto.endereco().localidade());
            endereco.setUf(dto.endereco().uf());
            endereco.setLat(dto.endereco().lat());
            endereco.setLng(dto.endereco().lng());
            endereco.setCep(dto.endereco().cep());
            entidade.setEndereco(endereco);

        }
    }

}
