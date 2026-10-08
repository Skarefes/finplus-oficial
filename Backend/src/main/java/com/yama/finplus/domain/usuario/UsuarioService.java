package com.yama.finplus.domain.usuario;

import com.yama.finplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    //Função de cadastro de usuario
    public DadosDetalhamentoUsuario cadastrandoUsuario(DadosCadastroUsuario dados) {
        var usuario = new Usuario(dados);
        usuario.setSenha(passwordEncoder.encode(dados.senha()));
        usuarioRepository.save(usuario);
        return new DadosDetalhamentoUsuario(usuario.getId(), usuario.getUsername(), usuario.getEmail());
    }

    @Transactional
    public void deletarUsuario(String email){
        var usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuario não encontrado"));

        usuarioRepository.delete(usuario);
    }
}
