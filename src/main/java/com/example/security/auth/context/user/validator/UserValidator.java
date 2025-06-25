package com.example.security.auth.context.user.validator;

import com.example.security.auth.exception.UserAlreadyExistsException;
import com.example.security.shared.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(username);
        }
    }
}
