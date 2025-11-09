package com.example.bankcard_api.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtill {
    
    

    private final long expirationsMs;
    private final SecretKey key;

    
    public JwtUtill(@Value("${token.signing.key}") String jwtSecret,
        @Value("${token.signing.expirationsMs}") Long expirationsMs){
        
        this.expirationsMs = expirationsMs;    
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, String role){
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationsMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public String exctractUsername(String token){
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String exctractRole(String token){
        Object r = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
        return r == null ? null : r.toString();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
