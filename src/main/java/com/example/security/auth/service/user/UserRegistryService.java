package com.example.security.auth.service.user;

import com.example.security.auth.api.protocol.request.RegistryRequest;
import com.example.security.auth.service.user.validator.UserValidator;
import com.example.security.common.protocol.response.MessageResponse;
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

    public MessageResponse registerUser(RegistryRequest request) {
        userValidator.validateUsername(request.username());
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(userAuthService.getRoleUser()));
        userAuthService.save(user);
        return new MessageResponse("User registered successfully!");
    }

}
