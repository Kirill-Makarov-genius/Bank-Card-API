package com.example.bankcard_api.service;


import static org.junit.jupiter.api.Assertions.*;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.bankcard_api.enums.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtServiceTest {


    private JwtService jwtService;
    private final long expirationsMs;
    private final String jwtSecret;

    
    public JwtServiceTest(){
        this.jwtSecret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";    
        this.expirationsMs = 86400000;
    }
    private
    @BeforeEach
    void setup() {
        jwtService = new JwtService(jwtSecret, expirationsMs);
    }

    @Test
    void testGenerateAndValidateToken() {
        String username = "user111";
        Role role = Role.USER;
        String token = jwtService.generateToken(username, role.name());

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
        assertEquals(username, jwtService.exctractUsername(token));
    }

    @Test
    void testExtractRoleReturnsNullIfNotPresent() {
    String token = Jwts.builder()
                       .setSubject("user2")
                       .signWith(jwtService.getKey(), SignatureAlgorithm.HS256)
                       .compact();

    assertNull(jwtService.extractRole(token));
    }
}

