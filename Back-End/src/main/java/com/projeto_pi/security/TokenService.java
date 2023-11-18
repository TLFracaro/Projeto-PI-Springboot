package com.projeto_pi.security;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.projeto_pi.models.Usuario;

@Service
public class TokenService {

    private String secret = "5BF946E7518FB974C980B89B660FBBB9CF4B96B658CADA0A9FEA4FE8F0E33378";

    public String generateToken(Usuario user) throws Exception {
        try {
            return JWT.create()
                    .withIssuer("thinkshareapi")
                    .withSubject(user.getEmail())
                    .withExpiresAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(2).toInstant())
                    .withClaim("privilegio", user.getPrivilegio().name())
                    .withClaim("usuarioId", user.getUsuarioId().toString())
                    .sign(Algorithm.HMAC256(secret));
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("thinkshareapi")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (Exception e) {
            throw e;
        }
    }
}
