package com.example.security.auth.context.refreshtoken.service;

import com.example.security.auth.context.refreshtoken.model.RefreshTokenRepository;
import com.example.security.auth.context.refreshtoken.properties.RefreshTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenCleaner {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenProperties refreshTokenProperties;

    @Scheduled(fixedDelayString = "#{@refreshTokenCleaner.cleanupInterval}")
    public void cleanExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens.");
        refreshTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }

    public long getCleanupInterval() {
        return refreshTokenProperties.getCleanupInterval();
    }
}

