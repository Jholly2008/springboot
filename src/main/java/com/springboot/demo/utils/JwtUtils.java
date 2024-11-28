package com.springboot.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {

    private static SecretKey getSigningKey() {
        String jwtSecret = "mySuperSecretKey123!@#$%^&*()_+-=[]{}|;:,.<>?";
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateToken(String username) {
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

    public static String getTenantFromToken(String token) {
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

    public static boolean validateToken(String token) {
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