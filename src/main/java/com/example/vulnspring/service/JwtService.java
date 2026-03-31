package com.example.vulnspring.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.vulnspring.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Component
public class JwtService {
    @Value("${jwt.secret:your-secret-key-must-be-at-least-256-bits}")
    private String secret;

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .withIssuer("vulnspring")
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }
}
