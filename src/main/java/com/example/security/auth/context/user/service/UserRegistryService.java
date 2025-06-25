package com.example.security.auth.context.user.service;

import com.example.security.auth.api.protocol.request.RegistryRequest;
import com.example.security.auth.context.hashing.service.SaltPepperHashService;
import com.example.security.auth.context.user.validator.UserValidator;
import com.example.security.shared.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRegistryService {
    private final UserAuthService userAuthService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final SaltPepperHashService hashService;

    public String registerUser(RegistryRequest request) {
        userValidator.validateUsername(request.username());
        User user = new User();
        char[] salt = hashService.generateSalt();
        user.setUsername(request.username());
        user.setPassword(preparePassword(request.password(), salt));
        user.setSalt(salt);
        user.setRoles(Set.of(userAuthService.getRoleUser()));
        userAuthService.save(user);
        return "User registered successfully!";
    }

    private String preparePassword(String password, char[] salt) {
        String enrichedPassword = hashService.enrich(password, salt);
        return passwordEncoder.encode(enrichedPassword);
    }
}
