package com.yama.finplus.infra.security.jwt;

import com.yama.finplus.domain.usuario.UsuarioDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.OneToMany;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    //filtro de segurança interna, no qual ele intercepta toda requisição http que chega na API
    //verifica se existem um Token valido e se tiver, autentica o usaurio no security
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");

        //captura e verifica no cabecalho de autorizacao
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extrai o token e do usuario
        String token = authHeader.substring(7);

        //Quando o usuario esta errado, token é mal informado expirado ou assinatura errada, vai gerar um erro
        try{
            String email = jwtService.extrairUsername(token);

            //busca dos detahles do usuario no banco de dados
            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

            //validacao to Token, ele faz uma analise sobre o token se expirou
            // e se o email no token bate com o que o userDetails retorna pelo banco
            if (jwtService.validarToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //Gera erro 401, que é o problema no usuario e nao no servidor
            return;
        }


        //passa a requisicao para o proximo filtro na corrente, no caso o controller para processar o endpoint
        filterChain.doFilter(request, response);


    }


}
