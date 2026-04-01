package com.example.vulnspring.service;

import com.example.vulnspring.RefreshTokenRepository;
import com.example.vulnspring.UserRepository;
import com.example.vulnspring.entity.RefreshToken;
import com.example.vulnspring.entity.User;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    @Value("${app.jwt.refresh-token-expiry:86400}")
    private long expirySeconds;

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusSeconds(expirySeconds));
        token.setRevoked(false);
        return repository.save(token);
    }

    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token invalid");
        }
        return refreshToken;
    }

    public void revokeAllUserTokens(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        repository.deleteByUser(user);
    }
    public void cleanupExpiredTokens() {
        repository.deleteByExpiryDateBefore(Instant.now());
        log.info("Cleaned up expired refresh tokens");
    }
}
