package com.haykz.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class for generating, extracting, and validating JWT (JSON Web Token).
 * <p>
 * This class uses HMAC SHA algorithms to sign and verify the JWT tokens. The token contains the username
 * as the subject and is signed with a secret key. The generated token is valid for one day.
 * </p>
 */
@Component
public class JWTUtil {
    @Value("${jwt.key}")
    private String secretKey;

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public Boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)));
    }
}
