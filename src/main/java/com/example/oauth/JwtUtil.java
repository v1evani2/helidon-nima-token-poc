package com.example.oauth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String createJwt(String clientId, int expirySeconds) {
        long nowMillis = System.currentTimeMillis();
        return Jwts.builder()
            .setSubject(clientId)
            .setIssuedAt(new Date(nowMillis))
            .setExpiration(new Date(nowMillis + expirySeconds * 1000))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }
}