package org.example.esg.infra.security;

import org.example.esg.infra.persistence.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails)this.usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
