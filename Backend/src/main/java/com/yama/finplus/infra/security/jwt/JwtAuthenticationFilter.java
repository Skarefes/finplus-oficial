package com.yama.finplus.infra.security.jwt;

import com.yama.finplus.domain.usuario.UsuarioDetailsService;
import jakarta.persistence.OneToMany;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //classe que vai entender quem é o usuario atravez do token
    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService1, UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService1;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    //filtro de segurança interna, no qual ele intercepta toda requisição http que chega na API
    //verifica se existem um Token valido e se tiver, autentica o usaurio no security
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");

        //captura e verifica no cabeçalho de autorização
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extrai o token e do usuario
        String token = authHeader.substring(7);
        String email = jwtService.extrairUsername(token);

        //busca dos detahles do usuario no banco de dados
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        //validação to Token, ele faz uma analise sobre o token se expirou
        // e se o email no token bate com o que o userDetails retorna pelo banco
        if (jwtService.validarToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        //passa a requisição para o próximo filtro na corrente, no caso o controller para processar o endpoint
        filterChain.doFilter(request, response);


    }


}
