package com.example.security.auth.service.refresh_token;

import com.example.security.auth.model.refreshtoken.RefreshTokenRepository;
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

    @Scheduled(fixedDelayString  = "${security.refresh-token.scheduler-clean-delay}")
    public void cleanExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens.");
        refreshTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}

