package com.yama.finplus.controller;

import com.yama.finplus.domain.usuario.DadosCadastroUsuario;
import com.yama.finplus.domain.usuario.DadosDetalhamentoUsuario;
import com.yama.finplus.domain.usuario.Usuario;
import com.yama.finplus.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/usuario")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrandoUsuario(@RequestBody @Valid DadosCadastroUsuario dadosCadastroUsuario){
        var criando = usuarioService.cadastrandoUsuario(dadosCadastroUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(criando);
    }

    //o ME, resolve para que ninguem alem da propria pessoa consiga apagar o proprio usaurio por isso ME invez de ID
    @DeleteMapping("/me")
    public ResponseEntity<Void> removerUsuario(Authentication authentication){
        String email = authentication.getName();
        usuarioService.deletarUsuario(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
