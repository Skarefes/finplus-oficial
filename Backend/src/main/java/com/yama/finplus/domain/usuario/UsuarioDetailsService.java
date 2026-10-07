package com.yama.finplus.domain.usuario;

import com.yama.finplus.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override //buscar no banco de dados o usuario e identificar no login, no caso é o email
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

        return new UsuarioDetails(usuario);
    }
}
