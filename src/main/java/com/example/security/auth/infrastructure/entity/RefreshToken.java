package com.example.security.auth.infrastructure.entity;

import com.example.security.shared.infrastructure.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private final static String REFRESH_TOKEN_SEQUENCE = "refresh_tokens_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = REFRESH_TOKEN_SEQUENCE)
    @SequenceGenerator(name = REFRESH_TOKEN_SEQUENCE, sequenceName = REFRESH_TOKEN_SEQUENCE, allocationSize = 1, initialValue = 1)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
