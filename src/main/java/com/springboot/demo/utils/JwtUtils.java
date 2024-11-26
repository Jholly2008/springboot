package com.springboot.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}") // 从配置文件读取密钥
    private String jwtSecret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 36000000);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .claim("tenant", username)
                .signWith(getSigningKey())
                .compact();

        return "Bearer " + token;
    }

    public String getTenantFromToken(String token) {
        try {
            String actualToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(actualToken)
                    .getPayload();
            return claims.get("tenant", String.class);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public boolean validateToken(String token) {
        try {
            String actualToken = token.replace("Bearer ", "");

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(actualToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}