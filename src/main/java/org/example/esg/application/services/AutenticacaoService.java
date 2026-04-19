package org.example.esg.application.services;

import org.example.esg.application.dtos.in.DadosAuthRequestDto;
import org.example.esg.application.dtos.in.RegisterDto;
import org.example.esg.application.dtos.out.DadosTokenJwtResponseDto;
import org.example.esg.application.mappers.EnderecoMapper;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.domain.entities.Usuario;
import org.example.esg.domain.exceptions.AutenticacaoFalhou;
import org.example.esg.domain.exceptions.EmailJaCadastrado;
import org.example.esg.infra.security.TokenService;
import org.example.esg.infra.persistence.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EnderecoMapper enderecoMapper;


    public DadosTokenJwtResponseDto autenticar(DadosAuthRequestDto dados) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            UserDetails userDetails = (UserDetails)this.authenticationManager.authenticate(authenticationToken).getPrincipal();
            String tokenJwt = this.tokenService.gerarToken(userDetails);
            return new DadosTokenJwtResponseDto(tokenJwt);
        } catch (AuthenticationException ex) {
            throw new AutenticacaoFalhou("Usuário ou senha inválidos");
        }
    }

    public Usuario registrar(RegisterDto registro) {
        if (usuarioRepository.existsByEmail(registro.email())) {
            throw new EmailJaCadastrado("Email já cadastrado!");
        }

        Endereco endereco = new Endereco();
        endereco.setCep(registro.enderecoDto().cep());
        endereco.setLogradouro(registro.enderecoDto().logradouro());
        endereco.setBairro(registro.enderecoDto().bairro());
        endereco.setLocalidade(registro.enderecoDto().localidade());
        endereco.setUf(registro.enderecoDto().uf());
        endereco.setLat(registro.enderecoDto().lat());
        endereco.setLng(registro.enderecoDto().lng());



        Usuario novo = new Usuario(
                registro.nome(),
                registro.email(),
                passwordEncoder.encode(registro.senha()),
                enderecoMapper.toDomain(registro.enderecoDto())
        );

        
        return usuarioRepository.save(novo);
    }


}
