package com.example.bankcard_api.service;

import java.nio.charset.StandardCharsets;

import java.util.Date;


import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.bankcard_api.exception.InvalidJwtException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
    
    

    private final long expirationsMs;
    private final SecretKey key;

    
    public JwtService(@Value("${token.signing.key}") String jwtSecret,
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
        try{
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch(JwtException e){
            throw new InvalidJwtException("Invalid jwt Exception", e);
        }
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

    public String getCurrentUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }


}
