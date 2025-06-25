package com.example.security.auth.context.refreshtoken.service;

import com.example.security.auth.api.protocol.request.RefreshTokenRequest;
import com.example.security.auth.api.protocol.response.RefreshTokenResponse;
import com.example.security.auth.context.refreshtoken.model.RefreshToken;
import com.example.security.auth.context.refreshtoken.model.RefreshTokenRepository;
import com.example.security.auth.context.refreshtoken.properties.RefreshTokenProperties;
import com.example.security.auth.exception.InvalidRefreshTokenException;
import com.example.security.auth.context.user.service.UserAuthService;
import com.example.security.auth.context.jwt.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserAuthService userAuthService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenProperties refreshTokenProperties;

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = findToken(request.refreshToken());
        String newJwtToken = jwtTokenService.generateToken(refreshToken.getUser());
        String newRefreshToken = createRefreshToken(refreshToken.getUser().getUsername());

        return new RefreshTokenResponse(newJwtToken, newRefreshToken);
    }

    public RefreshToken findToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh refreshToken."));

        if (isTokenExpired(refreshToken)) {
            deleteByToken(token);
            throw new InvalidRefreshTokenException("Refresh refreshToken has expired. Please log in again.");
        }
        return refreshToken;
    }

    public String createRefreshToken(String username) {
        deleteByUsername(username);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userAuthService.findByUsername(username));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenProperties.getExpirationTime()));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }

    public long getExpirationTime() {
        return refreshTokenProperties.getExpirationTime();
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    private void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }
}
