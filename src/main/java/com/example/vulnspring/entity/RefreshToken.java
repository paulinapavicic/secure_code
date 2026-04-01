package com.example.vulnspring.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token = UUID.randomUUID().toString();

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiryDate;

    private boolean revoked = false;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User user;
}
