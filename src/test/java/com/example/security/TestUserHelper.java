package com.example.security;

import com.example.security.auth.api.protocol.request.LoginRequest;
import com.example.security.auth.api.protocol.request.RegistryRequest;
import com.example.security.auth.api.protocol.response.LoginResponse;
import com.example.security.auth.model.refreshtoken.RefreshTokenRepository;
import com.example.security.auth.service.user.UserLoginService;
import com.example.security.auth.service.user.UserRegistryService;
import com.example.security.shared.model.user.UserRepository;
import com.example.security.shared.model.role.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestUserHelper {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRegistryService userRegistryService;
    private final UserLoginService userLoginService;

    public void registerUser(String username, String password) {
        var request = new RegistryRequest(username, password);
        userRegistryService.registerUser(request);
    }

    public LoginResponse loginUser(String username, String password) {
        var request = new LoginRequest(username, password);
        return userLoginService.login(request);
    }

    public LoginResponse registerAndLoginUser(String username, String password) {
        registerUser(username, password);
        return loginUser(username, password);
    }

    @Transactional
    public void cleanUsers() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
