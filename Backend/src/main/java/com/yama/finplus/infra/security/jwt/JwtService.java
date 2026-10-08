package com.yama.finplus.infra.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class JwtService {
    private final String secretKey;

    //valor da variavel chave criada por mim
    public JwtService(@Value("${jwt.secret}") String secrekey) {
        this.secretKey = secrekey;
    }

    //tempo de expiração do token = 1h
    private static final long EXPIRACAO_MS = 1000L * 60 * 60;

    public String gerarToken(UserDetails userDetails) {
        //Constroi uma JWT
        return Jwts.builder()
                //Ele sabe que o username é importante, e vai criar um token do usuario
                .subject(userDetails.getUsername()).issuedAt(new Date())
                //Aqui é quando o token vai expirar, no caso é em milisegundos
                .expiration(new Date(System.currentTimeMillis() + EXPIRACAO_MS))
                //assinatura que o servidor lê para perceber alterações no token
                .signWith(getKey())
                .compact();
    }

    //Leitor de token
    public String extrairUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    private SecretKey getKey() {
        //a chave, criptografada que eu criei, convertido em uma lista de bytes
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    //metodo que vailidar o token correspondnete ao usuario carregado
    public boolean validarToken(String token, UserDetails userDetails) {
        String username = extrairUsername(token);
        return username.equals(userDetails.getUsername());
    }


}
