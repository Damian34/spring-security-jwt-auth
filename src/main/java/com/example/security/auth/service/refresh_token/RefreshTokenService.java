package com.example.security.auth.service.refresh_token;

import com.example.security.auth.api.protocol.request.RefreshTokenRequest;
import com.example.security.auth.api.protocol.response.RefreshTokenResponse;
import com.example.security.auth.infrastructure.entity.RefreshToken;
import com.example.security.auth.infrastructure.repository.RefreshTokenRepository;
import com.example.security.auth.exception.InvalidRefreshTokenException;
import com.example.security.auth.service.user.UserAuthService;
import com.example.security.auth.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserAuthService userAuthService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${security.refresh-token.expiration-time}")
    private long refreshTokenExpiration;

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = findToken(request.refreshToken());
        String newJwtToken = jwtService.generateToken(refreshToken.getUser());
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
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }

    public long getExpirationTime() {
        return refreshTokenExpiration;
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    private void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUserUsername(username);
    }
}
