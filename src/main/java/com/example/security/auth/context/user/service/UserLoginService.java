package com.example.security.auth.context.user.service;

import com.example.security.auth.api.protocol.request.LoginRequest;
import com.example.security.auth.api.protocol.response.LoginResponse;
import com.example.security.auth.context.jwt.service.JwtTokenService;
import com.example.security.auth.context.refreshtoken.service.RefreshTokenService;
import com.example.security.shared.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    public LoginResponse login(LoginRequest request) {
        User authenticatedUser = authenticate(request);
        String jwtToken = jwtTokenService.generateToken(authenticatedUser);
        String refreshToken = refreshTokenService.createRefreshToken(authenticatedUser.getUsername());

        return new LoginResponse(
                authenticatedUser.getId(),
                authenticatedUser.getUsername(),
                authenticatedUser.getRoles().stream().map(role -> role.getName().toString()).toList(),
                jwtToken,
                jwtTokenService.getExpirationTime(),
                refreshToken,
                refreshTokenService.getExpirationTime()
        );
    }

    private User authenticate(LoginRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authenticationManager.authenticate(token);
        return userAuthService.findByUsername(request.username());
    }
}
