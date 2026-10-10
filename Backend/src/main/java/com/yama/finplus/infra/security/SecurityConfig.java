package com.yama.finplus.infra.security;

import com.yama.finplus.domain.usuario.UsuarioDetailsService;
import com.yama.finplus.infra.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public SecurityConfig(UsuarioDetailsService usuarioDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //metodo que quando alguem tentar autenticar, usar o UsuarioDetailsService para encontrar o usuario que ta pedindo
    //E o passwordEncoder do BCrypt para verificar a senha
    //DaoAuthentication é o padrão de chegagem de credenciais, recebe os dados do spring
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(usuarioDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) //desabilita o csrf
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()// pode ser acessado sem autenticação
                                .requestMatchers(HttpMethod.POST, "/usuario/registrar").permitAll()
                .anyRequest().authenticated()
                ) //Agora colocar o filtro no filtro
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //metodo que usa o autenticador do spring, gerenciador central de autenticação
    //responsavel por fazer o email e a senha autenticar e entrar
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
