package com.example.security.auth.api;

import com.example.security.auth.api.protocol.request.LoginRequest;
import com.example.security.auth.api.protocol.request.RegistryRequest;
import com.example.security.auth.api.protocol.request.RefreshTokenRequest;
import com.example.security.auth.api.protocol.response.LoginResponse;
import com.example.security.auth.service.refresh_token.RefreshTokenService;
import com.example.security.auth.service.user.UserLoginService;
import com.example.security.auth.service.user.UserRegistryService;
import com.example.security.common.protocol.response.MessageResponse;
import com.example.security.auth.api.protocol.response.RefreshTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserRegistryService userRegistryService;
    private final UserLoginService userLoginService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/registry")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody @Valid RegistryRequest request) {
        return ResponseEntity.ok(userRegistryService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userLoginService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

}
