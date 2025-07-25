package com.kosting.authservice.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.kosting.authservice.core.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    public String secret;

    private static final String ISSUER = "watch-list-security";

    private static final String ZoneOffSet = "-03:00";

    public String generateToken(User user)
    {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getLogin())
                    .withExpiresAt(dateExperience())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String token)
    {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return  JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception)
        {
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }

    public Instant dateExperience()
    {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of(ZoneOffSet));
    }

}
