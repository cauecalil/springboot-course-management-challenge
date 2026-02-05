package org.cauecalil.coursemanagement.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class JWTProvider {
    @Value("${security.token.secret}")
    private String secretKey;

    @Value("${security.token.expiration-minutes}")
    private long expirationMinutes;

    public TokenResultDTO generateToken(String subject, List<String> roles) {
        var expiresAt = Instant.now().plus(Duration.ofMinutes(expirationMinutes));

        var token = JWT.create()
                .withSubject(subject)
                .withClaim("roles", roles)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(secretKey));

        return TokenResultDTO.builder()
                .accessToken(token)
                .expiresAt(expiresAt.toEpochMilli())
                .build();
    }

    public DecodedJWT validateToken(String token) {
        token = token.replace("Bearer ", "");

        try {
            return JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException ex) {
            return null;
        }
    }
}
