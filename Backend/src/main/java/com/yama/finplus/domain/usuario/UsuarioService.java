package com.yama.finplus.domain.usuario;

import com.yama.finplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioRepository usuarioRepository1) {
        this.usuarioRepository = usuarioRepository1;
    }

    @Transactional
    //Função de cadastro de usuario
    public DadosDetalhamentoUsuario cadastrandoUsuario(DadosCadastroUsuario dados) {
        var usuario = new Usuario(dados);
        usuarioRepository.save(usuario);
        return new DadosDetalhamentoUsuario(usuario.getId(), usuario.getUsername(), usuario.getEmail());
    }
}
