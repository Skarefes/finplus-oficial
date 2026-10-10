package com.yama.finplus.controller;

import com.yama.finplus.domain.login.LoginRequest;
import com.yama.finplus.domain.login.LoginResponse;
import com.yama.finplus.domain.usuario.Usuario;
import com.yama.finplus.infra.security.jwt.JwtService;
import com.yama.finplus.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository userRepository;

    public AuthLoginController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    //meotod que vai logar e authenticar o usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                //userPassAuthToken e um envelope que carrega as credenciais do usuario durante o login com o spring
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha())
        );
        //Agora criar a ponte que vai altenticar o usuario e o jwt e logar com o token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.gerarToken(userDetails);

        //aqui o getUername() do spring e o email; com ele buscmaos o usuario, para pegar o nome
        Usuario usuario = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));


        return ResponseEntity.ok(new LoginResponse(token, usuario.getUsername()));
    }
}
