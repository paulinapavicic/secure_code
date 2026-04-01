package com.example.vulnspring;

import com.example.vulnspring.entity.RefreshToken;
import com.example.vulnspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
    void deleteByUser(User user);
    List<RefreshToken> findAllByUserUsername(String username);
    void deleteByExpiryDateBefore(Instant now);
}